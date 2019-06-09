def branches = [:]

// setup a latch
MAX_CONCURRENT = 10
latch = new java.util.concurrent.LinkedBlockingDeque(MAX_CONCURRENT)
// put a number of items into the queue to allow that number of branches to run
for (int i=0;i<MAX_CONCURRENT;i++) {
    latch.offer("$i")
}

for (int i=0; i < 21; i++) {
    def name = "$i"
    branches[name] = {
        def thing = null
        // this will not allow proceeding until there is something in the queue.
        waitUntil {
            thing = latch.pollFirst();
            return thing != null;
        }
        try {
            echo "Hello from $name"
            def timeout = (Math.random() * 10 + 1).intValue()
            echo "Sleep for $timeout seconds"
            sleep time: timeout, unit: 'SECONDS'
            echo "Goodbye from $name"
        }
        finally {
           // put something back into the queue to allow others to proceed
            latch.offer(thing)
        }
    }
}

timestamps {
    parallel branches
}