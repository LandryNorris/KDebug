KDebug
=====

Experimental code for introspecting Kotlin/Native Objects.
This library interacts with the internals of the Kotlin/Native 
runtime and is only guaranteed to work on Kotlin versions that
have been thoroughly tested.

Goals
=====

Working Kotlin Introspection
----------------------------

when passing in an ObjHeader*, the KotlinIntrospection functionality 
should be able to

[-] Get class name
[-] Determine if the type is an array
[ ] Get size taken up by object in memory

Validate memory regions
-----------------------

we can't be sure that the pointer we are give is valid. Because of this,
we should be able to check if a pointer is within a valid memory region

[ ] check if a pointer points to a valid region of memory

Malloc Introspection
--------------------

On some platforms, we can query malloc for allocations.

[ ] determine if a region of memory contains a valid Kotlin object
[ ] determine if a region of memory contains a valid Objective-C object

Ensure compatibility
--------------------

Test on different platforms and Kotlin versions