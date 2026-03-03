import argparse
import sys
import os
from healer.core.orchestrator import HealerOrchestrator

def main():
    parser = argparse.ArgumentParser(description="Professional Self-Healing Agent for RestAssured")
    parser.add_argument("--repo", default=".", help="Path to the repository to heal")
    parser.add_argument("--config", default="healer/config.yml", help="Path to config file")
    parser.add_argument("--test-filter", "--tests", help="Specific test class or method to run")
    parser.add_argument("--use-docker", action="store_true", help="Run tests in Docker container")
    parser.add_argument("--create-pr", action="store_true", help="Create a Pull Request with the fix")

    args = parser.parse_args()
    
    orchestrator = HealerOrchestrator(args.repo, args.config)
    success = orchestrator.run_repair_loop(
        test_filter=args.test_filter,
        use_docker=args.use_docker,
        create_pr=args.create_pr
    )
    
    sys.exit(0 if success else 1)

if __name__ == "__main__":
    main()
