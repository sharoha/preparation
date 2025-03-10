class Solution {
    public long[] findMaxSum(int[] nums1, int[] nums2, int k) {
        record Tuple(int n1, int i1, int n2) {
        }
        ;
        int n = nums1.length;
        Tuple[] tuples = new Tuple[n];
        for (int i = 0; i < n; i++) {
            tuples[i] = new Tuple(nums1[i], i, nums2[i]);
        }

        Arrays.sort(tuples, Comparator.comparingInt(Tuple::n1));
        long[] cum = new long[n];
        long runSum = 0;
        for (int i = 1; i < n; i++) {
            cum[i] = tuples[i - 1].n2 + runSum;
            runSum = cum[i];
        }
        System.out.println(Arrays.toString(tuples));
        var pq = new PriorityQueue<Long>();
        long[] ans = new long[n];
        long curSum = 0;
        for (int i = 0; i < n; i++) {
            if (i != 0 && tuples[i - 1].n1 == tuples[i].n1) {
                 if (pq.size() == k && pq.peek() < (long) tuples[i].n2) {
                    curSum -= pq.poll();
                    curSum += tuples[i].n2; 
                    pq.add((long) tuples[i].n2);
                } else if (pq.size() < k) {
                    pq.add((long) tuples[i].n2);
                    curSum += tuples[i].n2;
                }
                continue;
            }
            if (pq.isEmpty()) {
                ans[tuples[i].i1] = 0;
                pq.add((long) tuples[i].n2);
                curSum += tuples[i].n2;
            } else {
                ans[tuples[i].i1] = curSum;
                if (pq.size() == k && pq.peek() < (long) tuples[i].n2) {
                    curSum -= pq.poll();
                    curSum += tuples[i].n2; 
                    pq.add((long) tuples[i].n2);
                } else if (pq.size() < k) {
                    pq.add((long) tuples[i].n2);
                    curSum += tuples[i].n2;
                }
                System.out.println(pq);
            }
        }
        return ans;
    }
}©leetcode
