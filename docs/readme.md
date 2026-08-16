trace tables, diagrams, report drafts



Trace Table 1 — BST Search Path + Inorder Traversal

Use these 9 values:

50, 30, 70, 20, 40, 60, 80, 10, 35

After insertion, the BST becomes:

             50
           /    \
         30      70
        /  \    /  \
      20   40  60   80
     /    /
   10    35
A. BST Search-Path Trace

Lookup value: 35

Step	Current Node	Comparison	Decision	Next Node
1	50	35 < 50	Go left	30
2	30	35 > 30	Go right	40
3	40	35 < 40	Go left	35
4	35	35 = 35	Found	—

Search path:

50 → 30 → 40 → 35
B. BST Inorder Traversal

Inorder traversal follows:

Left → Root → Right
Step	Visited Node	Output
1	10	10
2	20	10, 20
3	30	10, 20, 30
4	35	10, 20, 30, 35
5	40	10, 20, 30, 35, 40
6	50	10, 20, 30, 35, 40, 50
7	60	10, 20, 30, 35, 40, 50, 60
8	70	10, 20, 30, 35, 40, 50, 60, 70
9	80	10, 20, 30, 35, 40, 50, 60, 70, 80

Final inorder output:

10, 20, 30, 35, 40, 50, 60, 70, 80

This demonstrates that inorder traversal of a BST produces the values in sorted order.

Trace Table 2 — Balanced vs Unbalanced Tree

The required input is:

1, 2, 3, 4, 5, 6, 7

Both trees receive exactly the same values in exactly the same order.

A. Plain Binary Search Tree

Because every new value is larger than the previous value, every new node goes to the right.

Insertion	Value Added	Resulting Height	Shape
1	1	1	1
2	2	2	1 → 2
3	3	3	1 → 2 → 3
4	4	4	1 → 2 → 3 → 4
5	5	5	1 → 2 → 3 → 4 → 5
6	6	6	1 → 2 → 3 → 4 → 5 → 6
7	7	7	1 → 2 → 3 → 4 → 5 → 6 → 7

Final tree:

1
 \
  2
   \
    3
     \
      4
       \
        5
         \
          6
           \
            7

Final BST height = 7

The tree has effectively degenerated into a linked list.

B. RedBlackTree — AVL-Style Balanced Tree

Your RedBlackTree implementation balances the tree using height and rotations.

Insertion	Value Added	Important Action	Resulting Height
1	1	No rotation needed	1
2	2	No rotation needed	2
3	3	RR → Left rotation	2
4	4	No rotation needed	3
5	5	RR → Left rotation	3
6	6	RR → Left rotation	3
7	7	RR → Left rotation	3

The final tree is:

        4
       / \
      2   6
     / \ / \
    1  3 5  7

Final RedBlackTree/AVL-style height = 3

Side-by-Side Comparison

This is the table I would definitely put in your report, because it directly addresses the requirement in your task.

Feature	BinarySearchTree	RedBlackTree (AVL-style)
Input order	1, 2, 3, 4, 5, 6, 7	1, 2, 3, 4, 5, 6, 7
Self-balancing	No	Yes
Rotations	None	Yes
Final height	7	3
Final shape	Straight line	Balanced
Worst-case height	O(n)	O(log n)
Visual comparison
        PLAIN BST                    AVL-STYLE TREE


            1                              4
             \                            /   \
              2                          2     6
               \                        / \   / \
                3                      1   3 5   7
                 \
                  4
                   \
                    5
                     \
                      6
                       \
                        7


              Height = 7                 Height = 3




Reports on BST,BTree and ReBlack Tree

Binary Search Tree

A Binary Search Tree (BST) is a tree-based data structure where each node contains one value. Values smaller than a node are placed in its left subtree, while larger values are placed in its right subtree. This ordering makes searching, insertion and deletion efficient when the tree is reasonably balanced. However, a normal BST does not automatically balance itself. If values are inserted in sorted order, the tree can degenerate into a linear structure, causing operations to become O(n) in the worst case.

The implementation supports insertion, removal, indexed access, replacement, size tracking and empty-state checking. Inorder traversal of the tree produces values in ascending order, demonstrating the main ordering property of a BST.

B-tree

A B-tree is a balanced multi-way search tree designed to store multiple keys in each node. Unlike a BST, where each node normally contains one key and has at most two children, a B-tree node can contain several keys and children. This reduces the height of the tree and makes B-trees particularly suitable for large datasets and disk-based storage.

The implementation uses a minimum degree of 2, meaning a node can contain up to three keys and four children. During insertion, full nodes are split. During deletion, the implementation handles removal from leaves and internal nodes, borrowing from siblings or merging nodes when necessary. These operations maintain the B-tree's balanced structure.

RedBlackTree / AVL-style Balanced Tree

The RedBlackTree implementation provided for this project is a simplified AVL-style self-balancing binary search tree rather than a conventional Red-Black Tree. Each node stores its height, and the balance factor is calculated as the height of the left subtree minus the height of the right subtree. If this value becomes greater than 1 or less than -1, rotations are used to restore balance.

There are four standard imbalance cases: left-left, right-right, left-right and right-left. A left-left imbalance is fixed with a right rotation, while a right-right imbalance is fixed with a left rotation. A left-right imbalance requires a left rotation on the left child followed by a right rotation. A right-left imbalance requires a right rotation on the right child followed by a left rotation.

The main advantage of this balancing is that the tree remains approximately O(log n) in height, even when values are inserted in sorted order. This can be demonstrated by inserting 1,2,3,4,5,6,7: the plain BST reaches height 7, while the AVL-style tree reaches height 3.