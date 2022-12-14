from traceback import print_exc
import unittest
import jep
from java.lang import Integer, String
import sys
from jep_pipe import jep_pipe, build_java_process_cmd
from java.io import FileInputStream


class TestExceptions(unittest.TestCase):

    def test_number_format(self):
        try:
            Integer.parseInt('asdf')
        except Exception as ex:
            self.assertIn('java.lang.NumberFormatException', str(ex))

    def test_io_exception(self):
        try:
            FileInputStream('asdf')
        except Exception as ex:
            self.assertIn('java.io.FileNotFoundException', str(ex))

    def test_null_pointer_exception(self):
        try:
            # throws http://stackoverflow.com/questions/3131865/why-does-string-valueofnull-throw-a-nullpointerexception
            String.valueOf(None)
        except Exception as ex:
            # because it's not a checked exception, mapped exceptions doesn't
            # apply here (all Runtime)
            self.assertIn('java.lang.NullPointerException', str(ex))


# the tests below verify that specific java exceptions map to python errors,
# enabling more precise except blocks when python encounters a java error

    def test_import_error(self):
        try:
            from java.lang import ArrayList
        except ImportError as ex:
            pass

    def test_index_error(self):
        x = jep.jarray(3, Integer)
        try:
            i = x[5]
        except IndexError as ex:
            pass

    def test_io_error(self):
        try:
            FileInputStream('asdf')
        except IOError as ex:
            pass

    def test_type_error(self):
        try:
            from java.util import Collections, ArrayList
            x = ArrayList()
            c = Collections.checkedList(x, Integer)
            c.add(Integer(5))
            c.add(String("5"))
        except TypeError as ex:
            pass

    def test_value_err(self):
        try:
            from java.lang import System
            System.getProperty('', '')
        except ValueError as ex:
            pass

    def test_arithmetic_error(self):
        try:
            from java.math import BigDecimal
            d = BigDecimal(3.14159)
            zero = BigDecimal(0.0)
            x = d.divide(zero)
        except ArithmeticError as ex:
            pass

    def test_exception_cause(self):
        jep_pipe(build_java_process_cmd('jep.test.TestExceptionCause'))

    # TODO come up with a way to test MemoryError and AssertionError given
    # I coded support for that.

    def test_java_exception_from_python_thread(self):
        # Calling java from a python thread has numerous limitations but since
        # there are some cases where it works we should be able to handle
        # exceptions properly.
        from java.lang import System
        from concurrent.futures import ThreadPoolExecutor
        def failOnOtherThread():
            # null is not allowed and will throw NPE
            return System.getenv(None)
        with ThreadPoolExecutor(max_workers=1) as executor:
            future = executor.submit(failOnOtherThread)
            with self.assertRaises(Exception) as raises:
                future.result()
            self.assertIn("NullPointerException", str(raises.exception))

