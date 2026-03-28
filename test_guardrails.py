import urllib.parse
from agent.core.guardrails import SecurityGuardrails
from agent.providers.gemini_generator import GeneratedFile

def test_guardrails():
    # Simulate valid file
    valid_file = GeneratedFile(path="src/main/java/com/abhinav/framework/controller/PlaceController.java", content="test")
    assert SecurityGuardrails.validate_generated_files([valid_file], "job_1") == True, "Valid file failed"
    print("✅ Valid file accepted")

    # Simulate path traversal
    invalid_file = GeneratedFile(path="../../build.gradle", content="malicious")
    assert SecurityGuardrails.validate_generated_files([invalid_file], "job_2") == False, "Path traversal bypassed guardrails"
    print("✅ Path traversal blocked")

    # Simulate blacklisted file
    blacklisted = GeneratedFile(path="pom.xml", content="malicious")
    assert SecurityGuardrails.validate_generated_files([blacklisted], "job_3") == False, "Blacklisted file bypassed guardrails"
    print("✅ Blacklisted file blocked")

    # Simulate bad extension (not .java) in logic folders
    bad_ext = GeneratedFile(path="src/main/java/com/abhinav/framework/test.txt", content="malicious")
    assert SecurityGuardrails.validate_generated_files([bad_ext], "job_4") == False, "Bad extension bypassed guardrails"
    print("✅ Unknown extension blocked")

if __name__ == "__main__":
    test_guardrails()
