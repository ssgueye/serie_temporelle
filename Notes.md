## Why are we using unidirectional mapping with `@ManyToOne` intead of bidirectional mapping with `@OneToMany` and `@ManyToOne`?
Both have advantages and disadvantages:
- Unidirectional mapping with `@ManyToOne` can avoid the potential performance issue of a `@OneToMany`. But it can not navigate or cascade CRUD operations to the child collections to the build collection. However, it can be done manually.
- Bidirectional mapping with `@OneToMany` and `@ManyToOne` can allow both entities  of the relationship quicky access and cascade CRUD operations to each other. Howerver, it can cause a performance issue on a large child collection.

According to this two comparisons, we decide to use the unidirectional mapping because we need to avoid performance issue. And for the cascade, we are going to do it manually.