# Prepared List of questions
1. Find next greater number with same set of digits: https://www.geeksforgeeks.org/find-next-greater-number-set-digits 
    a. Starting From right figure out the digit which is less than its next digit. This means that current digit can be used to find the next greater number.
    b. From this digit move towards right and figure of the smallest digit, swap that digit with the current one, and leaving the current swapped digit, reverse everything else.
    c. example: 54132  -> 54*1*32 -> 54*1*3*2* -> 54*2*3*1* -> 542*13*
