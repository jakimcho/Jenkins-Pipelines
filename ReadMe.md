# Jenkins Piplines

## Purpose

Here I'll collect all pipeline strategies that would be usefull for my and my projects

## TODO:

Currently when run ParallelWithPool scripted pipeline, there is increaseing waits per branch that comes from jenkins.

```
16:20:00  Will try again after 0.25 sec
16:20:00  Will try again after 0.25 sec
..............
16:20:15  Will try again after 0.3 sec
.......
16:20:15  Will try again after 0.3 sec
..........
16:20:22  Will try again after 0.36 sec
16:20:22  Will try again after 0.36 sec
............
16:20:25  Will try again after 0.43 sec
...........
16:20:29  Will try again after 0.51 sec
```

Need to find a way to set this waits to a static duration
