import java.util.Arrays;

public class IntList {

    private int pose;
    private int[] intList;

    public IntList() {
        this.pose = -1;
        this.intList = new int[16];
    }

    public IntList(int[] intList) {
        this.pose = intList.length - 1;
        this.intList = intList;
    }

    public int getPose() {
        return pose;
    }

    public int[] getIntList() {
        return intList;
    }

    public void add(int intValue) {
        pose++;
        if (pose >= intList.length) {
            intList = Arrays.copyOf(intList, intList.length*2);
        }
        intList[pose] = intValue;
    }

    public int get(int index) {
        return intList[index];
    }

    public void remove() {
        pose--;
        if (pose < intList.length/2) {
            intList = Arrays.copyOf(intList, intList.length/2);
        }
    }

    public int size() {
        return (pose + 1);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= pose; i++) {
            sb.append(intList[i] + " ");
        }
        return sb.toString().substring(0, sb.length() - 1);
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IntList) {
            IntList ints = (IntList) obj;
            return Arrays.copyOfRange(ints.getIntList(), 0, ints.size()).equals(Arrays.copyOfRange(intList, 0, size()));
        } else {
            return false;
        }
    }
    @Override
    public int hashCode() {
        return Arrays.hashCode(Arrays.copyOfRange(intList, 0, size()));
    }
}
