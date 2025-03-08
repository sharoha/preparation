class LeetCode4 {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        // hell yes its a difficult problem as ..
        // the idea is to find a partition in nums1 and nums2, such that
        // count(left(nums1)) + count(left(nums2)) = count(right(nums1)) +
        // count(right(nums2))
        // i.e number of elements in left partition is equal to the number of elements
        // in right partition. note that both array partition is considered here.
        // this condition is equally important as this makes sure that we always work
        // with smaller array in nums1
        if (nums1.length > nums2.length) {
            return findMedianSortedArrays(nums2, nums1);
        }
        int x = nums1.length;
        int y = nums2.length;

        int lo = 0;
        int hi = x;
        while (lo <= hi) {
            int partitionX = (lo + hi) / 2;
            int partitionY = (x + y + 1) / 2 - partitionX;
            // actually (x + y + 1) / 2 is the total number of elements in left side
            // get minLeftX, maxRightX, minLeftY, maxRightY, also handling the edge cases
            int maxLeftX = partitionX == 0 ? Integer.MIN_VALUE : nums1[partitionX - 1];
            int minRightX = partitionX == x ? Integer.MAX_VALUE : nums1[partitionX];

            int maxLeftY = partitionY == 0 ? Integer.MIN_VALUE : nums2[partitionY - 1];
            int minRightY = partitionY == y ? Integer.MAX_VALUE : nums2[partitionY];

            // if all elements in left side are less than equal to right elements
            if (maxLeftX <= minRightY && maxLeftY <= minRightX) {
                // we found the partition where left elements count is equal to right element
                // count, now the answer depends on the combined number of elements
                // if odd then left half will have one extra element than the right segment
                if ((x + y) % 2 == 1) {
                    return (double) Math.max(maxLeftX, maxLeftY);
                } else {
                    return (double) (Math.max(maxLeftX, maxLeftY) + Math.min(minRightX, minRightY)) / 2.0;
                }
            } else if (maxLeftX > minRightY) {
                hi = partitionX - 1;
            } else {
                lo = partitionX + 1;
            }
        }
        return 0.0;
    }
}
