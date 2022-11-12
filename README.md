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

Malloc Introspection
--------------------

On some platforms, we can query malloc for allocations.

[ ] determine if a region of memory contains a valid Kotlin object
[ ] determine if a region of memory contains a valid Objective-C object

Ensure compatibility
--------------------

Test on different platforms and Kotlin versions