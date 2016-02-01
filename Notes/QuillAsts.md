```
query[Circle].map(c => c.radius * 2)

=>

Map(
    Entity(Circle,None,List()),
    Ident(c),
    BinaryOperation(Property(Ident(c),radius),*,Constant(2)))

---

for {
    p <- query[Person]
} yield { (p.id) }

eval =>

Map(
    Entity(Person,None,List()),
    Ident(p),
    Property(Ident(p),id))

---

for {
    p <- query[Person] if(p.name == "Michael Bluth")
} yield { (p.id) }

=>

Map(
    Filter(
        Entity(Person,None,List()),
        Ident(p),
        BinaryOperation(Property(Ident(p),name),==,Constant(Michael Bluth))),
    Ident(p),
    Property(Ident(p),id))

---

val q = quote {
  for {
    p <- query[Person] if(p.id == 999)
    c <- query[Contact] if(c.personId == p.id)
  } yield {
    (p.name, c.phone)
  }
}

eval(q) =>

FlatMap(
	Filter(
		Entity(Person,None,List()),
		Ident(p),
		BinaryOperation(Property(Ident(p),id),==,Constant(999))),
	Ident(p),
	Map(
		Filter(
			Entity(Contact,None,List()),
			Ident(c),
			BinaryOperation(Property(Ident(c),personId),==,Property(Ident(p),id))),
	Ident(c),
	Tuple(
		List(Property(Ident(p),name), Property(Ident(c),phone)))))

---

val existsAny = quote {
  new {
    def apply[T](xs: Query[T])(p: T => Boolean) =
        xs.filter(p(_)).nonEmpty
  }
}

val q = quote {
  query[Circle].filter { c1 => 
    existsAny(query[Circle])(c2 => c2.radius > c1.radius)
  }
}

eval(q) =>

(
	Entity(Circle,None,List()),
	Ident(c1),
	FunctionApply(
		Function(
			List(Ident(xs), Ident(p)),
			UnaryOperation(
				nonEmpty,
				Filter(
					Ident(xs),
					Ident(x$1),
					FunctionApply(
						Ident(p),
						List(Ident(x$1)))))),
		List(
			Entity(Circle,None,List()), 
			Function(
				List(Ident(c2)),
				BinaryOperation(
					Property(Ident(c2),radius),
					>,
					Property(Ident(c1),radius))))))

---


val r = quote {
  query[Person].map(p => p.age)
}

val q = quote { r.min }

eval(q) =>

Aggregation(
	min,
	Map(
		Entity(Person,None,List()),
		Ident(p),
		Property(Ident(p),age)))

---

val r = quote {
  query[Person].map(p => p.name) // !!
}

val q = quote { r.min }

eval(q) => error: could not find implicit value for parameter n: Numeric[String] :)

--> Check this with db.run(q)

---

val q = quote {
  for {
      p <- for { d <- query[Person] if(d.id == 999) } yield { d }
      c <- query[Contact] if(c.personId == p.id)
  } yield {
      (p.name, c.phone)
  }
}

paste(q) =>

FlatMap(
	Map(
		Filter(
			Entity(Person,None,List()),
			Ident(d),
			BinaryOperation(Property(Ident(d),id),==,Constant(999))),
		Ident(d),
		Ident(d)),
	Ident(p),
	Map(
		Filter(
			Entity(Contact,None,List()),
			Ident(c),
			BinaryOperation(Property(Ident(c),personId),==,Property(Ident(p),id))),
		Ident(c),
		Tuple(List(Property(Ident(p),name), Property(Ident(c),phone)))))



```

