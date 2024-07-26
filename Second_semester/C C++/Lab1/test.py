import os
import subprocess
import unittest

from tqdm import tqdm


class TestProgram(unittest.TestCase):
    def test_case(self, filename):
        print(filename)
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
            result = subprocess.run(["main.exe"] + current[:-1], capture_output=True, text=True)
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
            result = subprocess.run(["main.exe"] + current[:-1], capture_output=True,
                                    text=True)
            expected_output = current[-1]
            if expected_output != result.stdout.strip():
                output.write(f"FAIL {count} {current[2]} {expected_output} {result.stdout.strip()}")
                print(f"FAIL {count} {current[2]} {expected_output} {result.stdout.strip()}")
                failed += 1

        print("Failed tests: " + str(failed))

tester = TestProgram()
#tester.test_case_bios()
#DIV
#tester.test_case("testDiv0.txt")
#tester.test_case("testDiv1.txt")
#tester.test_case("testDiv2.txt")
#tester.test_case("testDiv3.txt")
#MUL
#tester.test_case("testMul0S.txt")
#tester.test_case("testMul1.txt")
#tester.test_case("testMul2S.txt")
#tester.test_case("testMul3.txt")
#ADD
#tester.test_case("testAdd0S.txt")
#tester.test_case("testAdd1S.txt")
#tester.test_case("testAdd2.txt")
#tester.test_case("testAdd3.txt")
#SUB
#tester.test_case("testSub0.txt")
#tester.test_case("testSub1.txt")
#tester.test_case("testSub2.txt")
#tester.test_case("testSub3.txt")
#HALFMUL
#tester.test_case("testHalfMul0.txt")



