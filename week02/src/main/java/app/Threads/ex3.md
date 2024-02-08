The list is not added to syncanly so we hit a rase condition so one thread can override what a nother thread did
you culd syncarnise the addCount method

1. Stupid solution: only have 1 thread
2. Less stupid more canser: make a lock and have it as an argument, see ex3.java
