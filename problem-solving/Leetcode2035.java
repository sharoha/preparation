class Solution {
    // Problem_link : https://leetcode.com/problems/partition-array-into-two-arrays-to-minimize-sum-difference/
    // THe implementation is a bit tricky, possible ideas include: 
    // 1. Meet in the middle, n = 30 is a big giveaway, 
    // 2. Divide the array into two half
    // 3. calculate all possibel sums in each half, along with the number of elements required for each possible sum
    // 4. If you use a size of k from the first half, the next half should have (n - k) elements, 
    // 5. For each element of size k in first half, check the possible values to minimize the difference
    // 6. Need to find => 2 *  (first_half_ele + second_half_ele) - sum(nums)
    // 7. second_half_ele can be efficiently found using a binary search approach, the target value required to be found is, required = (sum / 2) - first_half_note, so we check if this values highest value <= required, and lowest value <= required 
    public int minimumDifference(int[] nums) {
        int n = nums.length / 2;
        var set1 = getAllSum(n, true, nums);
        var set2 = getAllSum(n, false, nums);
;
        int sum = 0;
        for (int u: nums) sum += u;
        int targetSum = sum / 2; 
        int ans = Integer.MAX_VALUE;
        for (int i = 0; i <= n; i++) {
            for (Integer ele: set1.get(i)) {

                // handle all the cases , i == n means that only the currently element will be used
                if (i == n) {
                    ans = Math.min(ans, Math.abs(2 * ele - sum));
                    continue;
                }


                int curTarget = targetSum - ele;
                Integer ceiling = set2.get(n - i).ceiling(curTarget);
                Integer floor = set2.get(n - i).floor(curTarget);
                // i == 0 means that only the ceiling and floor value should be considered for eveluating the difference
                if (i == 0) {
                    ans = ceiling == null ? ans : Math.abs(2 *  ceiling - sum);
                    ans = floor == null ? ans : Math.abs(2 * floor - sum);
                    continue;
                }
               // otherwise check for both ceiling and floor presence and calcualte 
                if (ceiling != null) {
                    ans = Math.min(Math.abs(2 *  (ceiling + ele) - sum), ans);
                }
                if (floor != null) {
                    ans = Math.min(Math.abs(2 * (floor + ele) - sum), ans);
                }
            }
        }

        return ans;    
    }

// generate a Matrix of all sums with the number of elements to used to construct this sum    
    public List<TreeSet<Integer>> getAllSum(int n, boolean secondHalf, int[] nums) {
        var list = new ArrayList<TreeSet<Integer>>(n);
        for (int i = 0; i <= n; i++) {
            list.add(new TreeSet<>());
        }
        for (int i = 0; i < (1 << n); i++) {
           int count = 0;;
           int sum = 0;
           for (int j = 0; j < n; j++) {
               if ((i & (1 << j)) > 0) {
                   if (secondHalf) {
                        sum += nums[j + n];
                   } else {
                        sum+= nums[j];
                   }
                   count++;
               }

           }
            // sum = sum(nums[k]) where {k | k is in [kth bit of i is set]}
            // size = count
            //  
           list.get(count).add(sum);
       }
       return list;
    }
}
