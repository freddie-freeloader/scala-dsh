(let t = table(person, [id::Int, name::String], [[id]])::[(Int,String)] in ([ ((x::(Int,String)).2::String) | x <- (t::[(Int,String)])]::[String]))::[String]

(let t = table(person, [id::Int, name::String], [[id]])::[(Int,String)] in [ (x.name)::String | x <- t ])

let t = table(person, [id::Int, name::String], [[id]])::[(Int,String)]
in                                          -- ^^^^^^
[ (x.name)::String | x <- t ]               -- Primary key

let t = table(person, [id::Int, name::String, age::Int], [[id]])
in
[ ( [ (p1.age)::Int + (p2.age)::Int | p2 <- t ], (p1.name)::String ) | p1 <- t ] -- ::[([Int],String)]

-- Definition of table:
let t = table(person, [id::Int, name::String], [[id]])::[(Int,String)]
                                            -- ^^^^^^
in                                          -- Primary key
-- Query:
[ (p.name)::String | p <- t ]
