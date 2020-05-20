# Chapter - 1 - Reliable, scalable and maintainable applications

There are different types of data systems -  

1. Store data so that they, or another application, can find it again later (databases)
2. Remember the result of an expensive operation, to speed up reads (caches)
3. Allow users to search data by keyword or filter it in various ways (search indexes)
4. Send a message to another process, to be handled asynchronously (stream processing)
5. Periodically crunch a large amount of accumulated data (batch processing)

Three concerns that are most important in data systems -  

1. **Reliability** - An app should work corrrectly even in the face of adversity
2. **Scalability** - As the system grows, there should be reasonable ways of dealing with growth
3. **Maintainability** - Over time many people will work on the system and they should be able to work on it productively

## Reliability

For an app, typical expectations are

- Application performs the function that the user expected
- It can tolerate wrong user inputs
- Performance is good enough for the required use case, under the expected load and data volume
- System prevents any unautorized access and abuse

### Faults

The things that can go wrong are faults. It is usually defined as one of the component deviating fron the spec. They can be of following types -  

#### Hardware faults

Few examples of hardware fault are -

1. Powergrid blackout
2. Hard disk failure

Solutions -  

1. Add redundancy to hardware components
2. Dual power supplies in servers
3. Batteries and diesel generators for backup

There is a move toward systems that can tolerate the loss of entire machines, by using software fault-tolerance techniques in preference or in addition to hardware redundancy.

#### Software faults

Few examples of sofware errors are -

1. A bug that cause every instance of the server to crash
2. A runway process that uses up some shared resource - CPU time, memory, disk space, network bandwidth etc.
3. A service that the system depends upon slows down or starts sending corrupted messages.
4. Cascading failures

#### Human errors

Few examples of human errors are -

1. Configuartion errors by operators

Solutions

1. Design systems in a way that minimizes opprtunity for human error
2. Test thoroughly at all levels using integration and manual tests
3. Setup detailed and clear monitoring such as performance metrics and error rates.

## Scalability

Scalability is the term we use to describe a system’s ability to cope with increased load.

### Describing load

First, we need to succinctly describe the current load on the system; only then can we discuss growth questions (what happens if our load doubles?). Load can be described with a few numbers which we call load parameters. The best choice of parameters depends on the architecture of your system:  

1. Requests per second to a web server
2. Ratio of reads to writes in a database
3. Number of simultaneously active users in a chat room
4. Hit rate on a cache 

### Describing performance

Once we have described the load on your system, you can investigate what happens when the load increases.

1. When you increase a load parameter and keep the system resources (CPU, memory, network bandwidth, etc.) unchanged, how is the performance of your system affected?
2. When you increase a load parameter, how much do you need to increase the resources if you want to keep performance unchanged?

#### Measuring response time of an API

1. **Average response time** - It is the arithmatic mean of all response times. However, the mean is not a very good metric if you want to know your “typical” response time, because it doesn’t tell you how many users actually experienced that delay.
2. **Percentiles** - If you take your list of response times and sort it from fastest to slowest, then the median is the halfway point. This makes the median a good metric if you want to know how long users typically have to wait. They are commonly used in **Service Level Objectives (SLOs)** and **Service Level Agreements(SLAs)**. There are different percentiles that are commonly used -
    - p50 (Median)
    - p95
    - p99
    - p999 (They are the response time thresholds at which 95%, 99%, or 99.9% of requests are faster than that particular threshold)

### Approaches for Coping with Load

There are following approaches -  

1. Scaling up (vertical scaling, moving to a more powerful machine)
2. Scaling out (horizontal scaling, distributing the load across multiple smaller machines)
3. Hybrid approach (using several fairly powerful machines can still be simpler and cheaper than a large number of small virtual machines)

## Maintainability

 We can and should design software in such a way that it will hopefully minimize pain during maintenance, and thus avoid creating legacy software ourselves. To this end, we will pay particular attention to three design principles for software systems:

1. **Operability** -
Make it easy for operations teams to keep the system running smoothly.
2. **Simplicity** -
Make it easy for new engineers to understand the system, by removing as much complexity as possible from the system.
3. **Evolvability** -
Make it easy for engineers to make changes to the system in the future, adapting it for unanticipated use cases as requirements change. Also known as extensibility, modifiability, or plasticity
