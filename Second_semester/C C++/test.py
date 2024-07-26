import os
import subprocess
import unittest

from tqdm import tqdm


class TestProgram(unittest.TestCase):
    def test_case(self, filename):
        with open(filename, "r") as f:
            lines = f.readlines()
            f.close()

        output = open("output.txt", "w")

        count = 0
        failed = 0
        for i in tqdm(lines):
            count += 1
            current = i.replace("\t", " ")
            current = current.replace("\n", "")
            current = current.split()
            result = subprocess.run(["/Users/nikon/Documents/ITMO/Плюсы/lab1/main"] + current[:-1], capture_output=True, text=True)
            expected_output = current[-1]
            if expected_output != result.stdout.strip():
                output.write(f"FAIL {count} {current[2]} {current[4]} {expected_output} {result.stdout.strip()}")
                print(f"FAIL {count} {current[2]} {current[4]} {expected_output} {result.stdout.strip()}")
                failed += 1
                
        output.write("Failed tests: " + str(failed))

    def test_case_bios(self):
        with open("testPrint.txt", "r") as f:
            lines = f.readlines()
            f.close()

        output = open("output.txt", "w")

        count = 0
        failed = 0
        for i in tqdm(lines):
            count += 1
            current = i.replace("\t", " ")
            current = current.replace("\n", "")
            current = current.split()
            result = subprocess.run(["/Users/nikon/Documents/ITMO/Плюсы/lab1/main"] + current[:-1], capture_output=True, text=True)
            expected_output = current[-1]
            if expected_output != result.stdout.strip():
                output.write(f"FAIL {count} {current[2]} {expected_output} {result.stdout.strip()}")
                print(f"FAIL {count} {current[2]} {expected_output} {result.stdout.strip()}")
                failed += 1
                
        print("Failed tests: " + str(failed))

tester = TestProgram()
# tester.test_case_bios()
# tester.test_case("testMulZero.txt")
# tester.test_case("testMul.txt")
# tester.test_case("testMulPos.txt")
# tester.test_case("testMulNeg.txt")
tester.test_case("testDivZero.txt")