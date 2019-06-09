def branches = [:]
// List with existing job names that need to run in parallel
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

// Define the pool of tickets and max tickets number
MAX_CONCURRENT = 3
MAX_POLL_TIMEOUT = 1
TIME_UNITS = java.util.concurrent.TimeUnit.SECONDS
ticketsPool = new java.util.concurrent.LinkedBlockingDeque(MAX_CONCURRENT)

// put a number of tickets into the queue to allow that number of branches to run
for (int i=0; i<MAX_CONCURRENT; i++) {
  ticketsPool.offer("ticket $i")
}

for (int i = 0; i < jobs.length; i++) {
  def job = jobs[i];
  branches[job] = {
    def ticket = null
    // this will not allow proceeding while there are no tickets in the queue
    waitUntil {
      ticket = ticketsPool.pollFirst( MAX_POLL_TIMEOUT, TIME_UNITS );
      return ticket != null;
    }
    try {
      echo "Hello from $job that got \"$ticket\" ticket to join parallel run"
      build job
      echo "Goodbye from $job and ticket \"$ticket\" will be returned"
    }
    finally {
      // return the ticket back into the queue to allow others to proceed
      ticketsPool.offer(ticket)
    }
  }
}

timestamps {
  stage("Regression testing"){
    parallel branches
  }
}