import os
import subprocess

class Patcher:
    @classmethod
    def apply_patch(cls, repo_path, diff_content):
        patch_file = os.path.join(repo_path, "temp_fix.patch")
        with open(patch_file, "w") as f:
            f.write(diff_content)

        try:
            # We use --ignore-space-change to be more resilient to whitespace issues
            result = subprocess.run(
                ["git", "apply", "--3way", "--ignore-space-change", "temp_fix.patch"],
                cwd=repo_path,
                capture_output=True,
                text=True
            )
            if result.returncode == 0:
                return True
            
            print(f"⚠ git apply failed (Code: {result.returncode}). Attempting surgical fallback...")
            return cls.fallback_apply(repo_path, diff_content)
            
        except Exception as e:
            print(f"Error applying patch: {e}")
            return False
        finally:
            if os.path.exists(patch_file):
                os.remove(patch_file)

    @classmethod
    def fallback_apply(cls, repo_path, diff_content):
        """
        Robust fallback when git apply fails. Attempts to find the '-' lines and replace them
        with '+' lines, ignoring context lines that often cause 'corrupt patch' errors in LLMs.
        """
        lines = diff_content.split('\n')
        target_file = None
        for line in lines:
            if line.startswith('--- a/'):
                target_file = os.path.join(repo_path, line[6:].strip())
                break
        
        if not target_file or not os.path.exists(target_file):
            return False

        with open(target_file, 'r') as f:
            content = f.read()

        # Group lines into blocks of - and +
        # LLMs usually return one block for simple fixes
        to_remove = [l[1:].strip() for l in lines if l.startswith('-') and not l.startswith('---')]
        to_add = [l[1:].strip() for l in lines if l.startswith('+') and not l.startswith('+++')]

        if not to_remove:
            return False

        new_content = content
        # Try to replace each line pair
        # This is a very simple mapper, works best for 1:1 line fixes
        success = False
        import re
        
        for i, r_line in enumerate(to_remove):
            if not r_line: continue
            
            # Escape for regex
            escaped_r = re.escape(r_line)
            # Match the line with any leading/trailing whitespace
            pattern = rf"^[ \t]*{escaped_r}[ \t]*$"
            
            # If we have a matching + line, use it. Otherwise use empty.
            replacement = to_add[i] if i < len(to_add) else ""
            
            # Find and replace
            if re.search(pattern, new_content, re.MULTILINE):
                # Preserve some indentation if possible (heuristic)
                # For now, just use the original to_add content which LLM usually indents ok
                new_content = re.sub(pattern, replacement, new_content, count=1, flags=re.MULTILINE)
                success = True
                print(f"✓ Surgical fallback: Applied change for '{r_line}'")

        if success:
            with open(target_file, 'w') as f:
                f.write(new_content)
            return True

        return False

    @staticmethod
    def revert_changes(repo_path):
        subprocess.run(["git", "checkout", "."], cwd=repo_path)
