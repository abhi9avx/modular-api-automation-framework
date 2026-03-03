#!/bin/bash
export PYTHONPATH=$PYTHONPATH:.

# Check for virtual environment
if [ -d "healer/.venv" ]; then
    ./healer/.venv/bin/python3 healer/main.py "$@"
else
    python3 healer/main.py "$@"
fi
