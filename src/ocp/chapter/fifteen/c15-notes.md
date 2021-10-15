# Chapter 15 - Functional Programming

Java Stream API
- Describe the Stream interface and pipelines
- Use lambda expressions and method references

Built-in Functional Interfaces
- Use interfaces from the java.util.function package
- Use core functional interfaces including Predicate, Consumer, Function and Supplier
- Use primitive and binary variations of base interfaces of java.util.function package
  
Lambda Operations on Streams
- Extract stream data using map, peek and flatMap methods
- Search stream data using search findFirst, findAny, anyMatch, allMatch and noneMatch methods
- Use the Optional class
- Perform calculations using count, max, min, average and sum stream operations
- Sort a ccollection using lambda expressions
- Use Collectors with streams, including the groupingBy and partitioningBy operations

## Introduction

This chapter will be focusing on the Streams API. Note that Streams API in this chapter is used for functional programming. Many more functional classes and Optional classes will be introduced. Then the Stream pipeline to end it tieing it all together. This is a long chapter but the many objectives of it will cover similar topics.

## Working with Built-in Functional Interfaces (p.670-681)

The last chapter presented some basic functional interfaces that are used with the Collections Framework. Now, we will learn them in more detail and more thoroughly. Remember that a functional inteface has exactly one abstract method. 

Now we are going to look at some functional interfaces, that are provided in the java.util.function package.