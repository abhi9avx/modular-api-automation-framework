import os
import xml.etree.ElementTree as ET
from dataclasses import dataclass
from typing import List, Optional

@dataclass
class FailureContext:
    test_class: str
    test_name: str
    failure_type: str
    message: str
    stack_trace: str
    file_path: Optional[str] = None
    is_scripting_issue: bool = False

class FailureAnalyzer:
    def __init__(self, artifacts_path):
        self.artifacts_path = artifacts_path

    def analyze(self) -> List[FailureContext]:
        failures = []
        if not os.path.exists(self.artifacts_path):
            print(f"Artifacts path not found: {self.artifacts_path}")
            return []

        for root, _, files in os.walk(self.artifacts_path):
            for file in files:
                if (file.endswith(".xml") and (file.startswith("TEST-") or "testng-results" in file)):
                    file_path = os.path.join(root, file)
                    failures.extend(self._parse_file(file_path))
        
        return failures

    def _parse_file(self, xml_path) -> List[FailureContext]:
        contexts = []
        try:
            tree = ET.parse(xml_path)
            root = tree.getroot()
            
            # Support both JUnit (TEST-*.xml) and TestNG (testng-results.xml)
            if "testng-results" in xml_path:
                for suite in root.findall('.//suite'):
                    suite_name = suite.attrib.get('name', 'Unknown')
                    for testcase in suite.findall('.//test-method'):
                        status = testcase.attrib.get('status')
                        if status == 'FAIL':
                            test_name = testcase.attrib.get('name', 'Unknown')
                            exception = testcase.find('exception')
                            if exception is not None:
                                failure_type = exception.attrib.get('class', 'Unknown')
                                message = exception.findtext('message', '')
                                stack_trace = exception.findtext('full-stacktrace', '')
                                
                                is_scripting = self._classify_issue(failure_type, message, stack_trace)
                                
                                ctx = FailureContext(
                                    test_class=suite_name,
                                    test_name=test_name,
                                    failure_type=failure_type,
                                    message=message,
                                    stack_trace=stack_trace,
                                    is_scripting_issue=is_scripting
                                )
                                contexts.append(ctx)
            else:
                suites = [root] if root.tag == 'testsuite' else root.findall('testsuite')
                if root.tag == 'testsuites':
                    suites = root.findall('testsuite')

                for suite in suites:
                    test_class_name = suite.attrib.get('name', 'Unknown')
                    
                    for testcase in suite.findall('testcase'):
                        failure = testcase.find('failure')
                        error = testcase.find('error')
                        issue = failure if failure is not None else error
                        
                        if issue is not None:
                            test_name = testcase.attrib.get('name', 'Unknown')
                            failure_type = issue.attrib.get('type', 'Unknown')
                            message = issue.attrib.get('message', '')
                            stack_trace = issue.text or ""
                            
                            is_scripting = self._classify_issue(failure_type, message, stack_trace)
                            
                            ctx = FailureContext(
                                test_class=test_class_name,
                                test_name=test_name,
                                failure_type=failure_type,
                                message=message,
                                stack_trace=stack_trace,
                                is_scripting_issue=is_scripting
                            )
                            contexts.append(ctx)
        except ET.ParseError as e:
            print(f"Failed to parse XML {xml_path}: {e}")
        
        return contexts

    def _classify_issue(self, failure_type, message, stack_trace) -> bool:
        scripting_indicators = [
            "NoSuchElementException",
            "ElementNotFoundException",
            "StaleElementReferenceException",
            "AssertionError",
            "ComparisonFailure",
            "junit.framework.AssertionFailedError",
            "JsonPathException",
            "GroovyResultException"
        ]
        
        system_indicators = [
            "ConnectException",
            "SocketTimeoutException",
            "OutOfMemoryError",
            "SQLException"
        ]
        
        full_text = f"{failure_type} {message} {stack_trace}"
        
        for indicator in system_indicators:
            if indicator in full_text:
                return False
                
        for indicator in scripting_indicators:
            if indicator in full_text:
                return True
                
        # For RestAssured, status code mismatches are scripting issues
        if "expected" in message.lower() and "but was" in message.lower():
            return True

        return False
