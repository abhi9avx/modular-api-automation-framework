import docker
import os

class DockerManager:
    def __init__(self, image_name="mcp-agent-runner", dockerfile_path="healer"):
        try:
            self.client = docker.from_env()
        except Exception as e:
            self.client = None
            print(f"Warning: Docker client could not be initialized: {e}")
            
        self.image_name = image_name
        self.dockerfile_path = dockerfile_path

    def check_docker(self):
        if not self.client:
            return False
        try:
            self.client.ping()
            return True
        except Exception:
            return False

    def build_image(self):
        print(f"Building Docker image {self.image_name}...")
        try:
            self.client.images.build(
                path=self.dockerfile_path,
                tag=self.image_name,
                dockerfile="Dockerfile.agent"
            )
            print("Image build successful.")
        except Exception as e:
            print(f"Error building image: {e}")
            raise

    def run_tests(self, repo_path, command=["./gradlew", "test"]):
        abs_repo_path = os.path.abspath(repo_path)
        print(f"Running tests in {abs_repo_path} using {self.image_name} in Docker...")
        
        try:
            container = self.client.containers.run(
                self.image_name,
                command=command,
                volumes={abs_repo_path: {'bind': '/workspace', 'mode': 'rw'}},
                working_dir="/workspace",
                detach=True,
                remove=False
            )
            
            result = container.wait()
            logs = container.logs().decode('utf-8')
            container.remove()
            
            return {
                "exit_code": result['StatusCode'],
                "logs": logs
            }
        except Exception as e:
            print(f"Error running container: {e}")
            raise
