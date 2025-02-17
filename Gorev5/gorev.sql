SELECT d.Name AS Department, COUNT(e.Id) AS Employee_Count
FROM Dept d
LEFT JOIN Emp e ON d.Id = e.DeptId
GROUP BY d.Name
ORDER BY Employee_Count DESC;
