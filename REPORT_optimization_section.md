Optimization Algorithms- Report Section
Greedy Scheduler and Dynamic Programming Optimizer (Opoku)

 1. Greedy Scheduler — Documented Counterexample

(Kept exactly as originally written — do not modify.)

Resource pool (fixed order): `Van-1 (VAN)`, `Rider-1 (RIDER)`
Requests:
- `R-Shuttle`: category = SHUTTLE, urgency = 10 (can use VAN or RIDER)
- `R-Medical`: category = MEDICAL, urgency = 9 (can use VAN only)

Greedy run: processes by urgency descending, so `R-Shuttle` (10) is handled
first. It matches the first available compatible resource in pool order,
which is `Van-1`. That leaves `Rider-1` — but `R-Medical` cannot use a Rider,
so it goes unassigned.

- Greedy total urgency served = 10 (R-Medical unserved)

Optimal assignment:`Rider-1 → R-Shuttle`, `Van-1 → R-Medical`. Both
requests are served.

- Optimal total urgency served = 19

Conclusion: Greedy served only 10 urgency units because it let the
flexible SHUTTLE request consume the only Van, even though a Rider could
have served it just as well. The optimal assignment reserves the
constrained resource (Van) for the request that has no alternative
(Medical), serving 19 units total. This is a classic greedy failure: a
local "first available match" choice ignores downstream resource
constraints — unlike interval/activity scheduling, where earliest-finish-
time greedy is provably optimal (see proof sketch, Section 3).

This scenario is captured as a permanent regression test:
`test Greedy Counterexample Regression` in `SchedulerAlgorithmsTest.java`.

---

2. Dynamic Programming Optimizer — DP Table Write-Up

Input: 5 requests, budget = 15

| Request | Cost | Value |
|---------|------|-------|
| R1      | 3    | 4     |
| R2      | 4    | 5     |
| R3      | 5    | 6     |
| R4      | 2    | 3     |
| R5      | 6    | 9     |

 Full DP Table

"dp [i][w] " = best achievable value using the first i requests within
budget w. Row 0 (no requests considered) is all zeros.

| i \ w   | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10 | 11 | 12 | 13 | 14 | 15 |
|---------|---|---|---|---|---|---|---|---|---|---|----|----|----|----|----|----|
| 0 (none)| 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0  | 0  | 0  | 0  | 0  | 0  |
| 1 (R1)  | 0 | 0 | 0 | 4 | 4 | 4 | 4 | 4 | 4 | 4 | 4  | 4  | 4  | 4  | 4  | 4  |
| 2 (R2)  | 0 | 0 | 0 | 4 | 5 | 5 | 5 | 9 | 9 | 9 | 9  | 9  | 9  | 9  | 9  | 9  |
| 3 (R3)  | 0 | 0 | 0 | 4 | 5 | 6 | 6 | 9 | 10| 11| 11 | 11 | 15 | 15 | 15 | 15 |
| 4 (R4)  | 0 | 0 | 3 | 4 | 5 | 7 | 8 | 9 | 10| 12| 13 | 14 | 15 | 15 | 18 | 18 |
| 5 (R5)  | 0 | 0 | 3 | 4 | 5 | 7 | 9 | 9 | 12| 13| 14 | 16 | 17 | 18 | 19 | 21 |

Maximum achievable value: `dp[5][15] = 21`

Solution Reconstruction (which requests, not just the number)

Backtrack from `dp[5][15]`, comparing each row to the row above it. If the
values differ, that row's request was included (and we subtract its cost
from the remaining budget before moving up); if they're equal, it was
skipped.

| Step | Compare              | Values      | Decision       | Remaining Budget |
|------|-----------------------|-------------|----------------|-------------------|
| 1    | dp[5][15] vs dp[4][15]| 21 vs 18    | R5 included | 15 − 6 = 9         |
| 2    | dp[4][9] vs dp[3][9]  | 12 vs 11    | R4 included| 9 − 2 = 7          |
| 3    | dp[3][7] vs dp[2][7]  | 9 vs 9      | R3 skipped      | 7                  |
| 4    | dp[2][7] vs dp[1][7]  | 9 vs 4      | R2 included| 7 − 4 = 3          |
| 5    | dp[1][3] vs dp[0][3]  | 4 vs 0      | R1 included | 3 − 3 = 0          |

Selected requests: R1, R2, R4, R5 (R3 is left out)
Total cost: 3 + 4 + 2 + 6 = 15 (exactly the budget)
Total value: 4 + 5 + 3 + 9 = 21 ✓ matches `dp[5][15]`

This exact case is asserted in `test Dp Five Request Budget Fifteen` in
`SchedulerAlgorithmsTest.java`.

---

 3. Proof Sketch — Why Greedy Fails Here but Works Elsewhere

(Opoku's proof sketch — required deliverable #3, tied to the counterexample above.)

The scheduler's greedy rule — "highest urgency first, first available
compatible resource" — is a local rule: at each step it only asks
"what's best for the request in front of me right now," with no notion of
what resources later requests will need. That's fine when resource
compatibility is uniform (every request could use every resource), but it
breaks down here because resources have asymmetric compatibility: a Van
can cover both SHUTTLE and MEDICAL, but a Rider can only cover SHUTTLE and
MAINTENANCE. When a flexible request (SHUTTLE) is processed before a
constrained one (MEDICAL), the greedy rule has no way to "know" it should
save the Van for the request that has no substitute — it just takes the
first match. Formally, greedy is only guaranteed optimal when the
underlying selection problem has the exchange property: any locally
optimal choice can be extended to a globally optimal solution without
loss (this is the matroid/greedy-optimality condition behind, e.g.,
Kruskal's MST algorithm and interval/activity scheduling by earliest
finish time). Resource-constrained assignment does not have this
property — swapping which resource serves a flexible request can strictly
increase how many other requests get served, which the counterexample
demonstrates directly (10 units served greedily vs. 19 optimally).

Contrast this with interval scheduling (picking the maximum number of
non-overlapping activities from a single resource, sorted by finish time):
there, the one formal step that makes greedy provably correct is an
exchange argument — if an optimal solution's first-chosen activity has a
later finish time than the earliest-finishing activity `a`, you can always
swap `a` in for it without invalidating the schedule or reducing the
count, because `a` finishes at least as early and therefore conflicts with
no more of the remaining activities than the one it replaced. Repeating
this swap inductively shows greedy choice can always be substituted into
some optimal solution, so greedy loses nothing. In the resource-matching
scheduler, no such swap argument holds: substituting resources for one
request changes what's available to a different, less flexible request,
so a locally "free" choice can foreclose a globally better one. That's
precisely why the scheduler needs either a smarter tie-breaking rule
(e.g., prefer the least-flexible compatible resource type first) or an
exact method like the Dynamic Programming Optimizer for cases where
optimality actually matters.
