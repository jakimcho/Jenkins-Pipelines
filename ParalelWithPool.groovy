def branches = [:]
String[] jobs = [
  "Project Package Job",
  "Project Test Deploy Job",
  "Project Test Integration Job",
  "Project Compiled Job",
  "Unit Test Job",
  "Project WIN UAT Testing Job",
  "Project CHROME UAT Testing Job",
  "Project Realease Job"
]

// setup a latch
MAX_CONCURRENT = 3
ticketsPool = new java.util.concurrent.LinkedBlockingDeque(MAX_CONCURRENT)
// put a number of items into the queue to allow that number of branches to run
for (int i=0; i<MAX_CONCURRENT; i++) {
    ticketsPool.offer("$i")
}

for (int i=0; i < jobs.length; i++) {
    def jobName = jobs[i]
    branches[jobName] = {
        def ticket = null
        // this will not allow proceeding while there are no tickets in the queue
        waitUntil {
            ticket = ticketsPool.pollFirst();
            return ticket != null;
        }
        try {
            echo "Hello from $jobName"
            // def timeout = (Math.random() * 10 + 1).intValue()
            // echo "Sleep for $timeout seconds"
            // sleep time: timeout, unit: 'SECONDS'
            build jobName
            echo "Goodbye from $jobName"
        }
        finally {
           // return the ticket back into the queue to allow others to proceed
            ticketsPool.offer(ticket)
        }
    }
}

timestamps {
    parallel branches
}