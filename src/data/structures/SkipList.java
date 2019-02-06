package data.structures;

import entitis.ShopOrder;

import java.util.Random;

/**
 * 跳表
 * @Author zxa
 */
public class SkipList<T extends Comparable<? super T>> {

    private static final int MAX_LEVEL = 16;

    private int levelCount = 1;

    private Node head = new Node(MAX_LEVEL);  // 带头链表

    private Random r = new Random();

    private int[] levelsCount = new int[MAX_LEVEL + 1];

    public Node find(T value) {
        int j=0;
        Node p = head;
        for (int i = levelCount - 1; i >= 0; --i) {
            while (p.forwards[i] != null && p.forwards[i].data.compareTo(value) < 0) {
                p = p.forwards[i];
                j++;
            }
        }
        if (p.forwards[0] != null && p.forwards[0].data.equals(value)) {
            return p.forwards[0];
        } else {
            return null;
        }
    }

    public SkipList insert(T value) {
        int level = randomLevel();
        Node newNode = new Node(level);
        newNode.data = value;
        newNode.maxLevel = level;
        Node update[] = new Node[level];
        for (int i = 0; i < level; ++i) {
            update[i] = head;
        }

        // record every level largest value which smaller than insert value in update[]
        Node p = head;
        for (int i = level - 1; i >= 0; --i) {
            while (p.forwards[i] != null && p.forwards[i].data.compareTo(value) < 0) {
                p = p.forwards[i];
            }
            update[i] = p;// use update save node in search path
        }

        // in search path node next node become new node forwords(next)
        for (int i = 0; i < level; ++i) {
            newNode.forwards[i] = update[i].forwards[i];
            update[i].forwards[i] = newNode;
        }
        levelsCount[level]++;
        // update node hight
        if (levelCount < level) levelCount = level;

        return this;
    }

    public void delete(T value) {
        Node[] update = new Node[levelCount];
        Node p = head;
        for (int i = levelCount - 1; i >= 0; --i) {
            while (p.forwards[i] != null && p.forwards[i].data.compareTo(value) < 0) {
                p = p.forwards[i];
            }
            update[i] = p;
        }
        /**将上一节点的数据指向下一节点*/
        int nodeLevel = 0;
        if (p.forwards[0] != null && p.forwards[0].data.equals(value)) {
            for (int i = levelCount - 1; i >= 0; --i) {
                if (update[i].forwards[i] != null && update[i].forwards[i].data.compareTo(value) == 0) {
                    update[i].forwards[i] = update[i].forwards[i].forwards[i];
                    nodeLevel++;
                }
            }
        }
        levelsCount[nodeLevel]--;
    }

    // 随机 level 次，如果是奇数层数 +1，防止伪随机
    private int randomLevel() {
        int level = 1;
        for (int i = MAX_LEVEL; i > 1; i--) {
            if (levelsCount[i] == 0 || levelsCount[i] * 2 <= levelsCount[i - 1]) {
                level = i;
                break;
            }
        }
        return level;
    }

    public void printAll() {
        Node p = head;
        while (p.forwards[0] != null) {
            System.out.println(p.forwards[0] + " ");
            p = p.forwards[0];
        }
    }

    public void printBetween(T min, T max) {
        Node p = head;
        while (p.forwards[0] != null) {
            if (p.forwards[0].data.compareTo(min) >= 0 && p.forwards[0].data.compareTo(max) <= 0) {
                System.out.println(p.forwards[0] + " ");
            }
            p = p.forwards[0];
        }
    }

    private class Node<T extends Comparable<? super T>> {
        private T data = null;
        private Node forwards[];
        private int maxLevel = 0;

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("{ data: ");
            builder.append(data);
            builder.append("; levels: ");
            builder.append(maxLevel);
            builder.append(" }");

            return builder.toString();
        }

        public Node(int level) {
            if (level > MAX_LEVEL) {
                throw new IllegalStateException();
            }
            if (level < 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            this.forwards = new Node[level];
        }
    }


    public static void main(String[] args) {
        Random r = new Random();
        SkipList skipList = new SkipList();
        for (int i = 0; i <= 10000; i++) {
            skipList.insert(r.nextInt(10000));
        }
        skipList.printAll();
    }
}
