public class Q3 {
    public static void main(String[] args) {
        var q3 = new Q3();
        int[] fruits = { 4, 2, 5 };
        int[] baskets = { 3, 5, 4 };
        int count = q3.numOfUnplacedFruits(fruits, baskets);
        System.out.println(count);
    }

    public int numOfUnplacedFruits(int[] fruits, int[] baskets) {
        var segTree = new SegTree(fruits.length, baskets);
        int n = fruits.length;
        int ans = 0;
        for (int i = 0; i < fruits.length; i++) {
            int pos = segTree.query(1, 0, n - 1, fruits[i]);
            if (pos == -1) {
                ans++;
            } else {
                segTree.update(1, 0, n - 1, pos);
            }
        }
        return ans;
    }

    static class SegTree {
        private int[] segment;
        private int n;
        private int[] lo;
        private int[] hi;

        public SegTree(int n, int[] nums) {
            while (Integer.bitCount(n) != 1) {
                n++;
            }
            this.n = n;
            // here second dimension in segment array means
            // index 0 -> take first take last // index 1 - // index 2 -> take last
            // index 3 -> skip first skip last
            segment = new int[2 * n + 1];
            lo = new int[2 * n + 1];
            hi = new int[2 * n + 1];
            init(1, 0, n - 1);
            buildTree(nums);
        }

        // should return the position where the stored value is just higher than x

        public int query(int v, int l, int r, int x) {
            if (l > hi[v] || r < lo[v])
                return -1;
            if (segment[v] < x)
                return -1;
            if (l == r)
                return l;
            int m = (l + r) / 2;
            if (segment[2 * v] >= x) {
                // if left node is greater than equal to x
                return query(2 * v, l, m, x);
            } else {
                return query(2 * v + 1, m + 1, r, x);
            }
        }

        public void update(int v, int l, int r, int ind) {
            if (l > hi[v] || r < lo[v])
                return;
            if (l == r) {
                segment[l] = -1;
                return;
            }

            int m = (l + r) / 2;
            if (ind <= m) {
                update(2 * v, l, m, ind);
            } else {
                update(2 * v + 1, m + 1, r, ind);
            }
            segment[v] = Math.max(segment[2 * v], segment[2 * v + 1]);

        }

        public void buildTree(int[] nums) {
            for (int i = 0; i < nums.length; i++) {
                segment[n + i] = nums[i];
            }
            buildTree(1, 0, n - 1);
        }

        public void buildTree(int v, int l, int r) {
            if (l > hi[v] || r < lo[v])
                return;
            if (l == r)
                return;
            int m = (l + r) / 2;
            buildTree(2 * v, l, m);
            buildTree(2 * v + 1, m + 1, r);
            segment[v] = Math.max(segment[2 * v], segment[2 * v + 1]);
        }

        // initialize the range for each vertex
        public void init(int v, int l, int h) {
            lo[v] = l;
            hi[v] = h;
            if (l == h)
                return;
            int m = (l + h) / 2;
            init(2 * v, l, m);
            init(2 * v + 1, m + 1, h);
        }
    }

}
