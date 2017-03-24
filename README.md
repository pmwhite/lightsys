# LightSys Communications Research Project

From project description:

The solution to communications security is often viewed as resting entirely on encryption. However, encryption often obscures the "trees" while leaving the proverbial "forest" in plain view. In other words, individual messages and content are encrypted but the communications metadata is far more revealing than often thought. This project is a proof-of-concept to demonstrate the dangers of focusing on encryption while ignoring the metadata problem.

What we were given to make sense of were packet files in csv form. The only information we had was the time the packet was recieved, the length of the packet, the direction the packet was heading, and the anonymous ip addresses (we were given animal names instead of actual ip addresses). 

Our task was to take all of the packets from the whole week at Code-A-Thon and predict which animals were on the same teams, or were perhaps correlated in some way.

## Process

We tried a number of things before hitting on some good ideas that seemed to work pretty well. Our initial attempts included using premade clustering algorithms directly on the packets to see if something interesting showed up. None of our initial attempts gave any results, but they did serve to get our minds working and understanding exactly what we needed to do.

The first breakthrough idea was to create a table of animals vs packet lengths with each cell containing the number of packet lengths that animal received. Although this didn't yield any good results in the beginning, we ended up using a similar technique when we had some success.

Our biggest problem was that we were trying to correlate raw packet data, but this data has almost know similarities between people on the same team. The second breakthrough was when we began reconstructing the packets to their original sizes before the breakdown the TCP requires. By doing this before creating the table, we greatly increased what a packet size coincidence could mean. If two animals get the same reconstructed packet size, that means that the resource they are getting from the internet (a web page perhaps) has essentially the same size, which makes it very possible that it is the the same resource.

Here is an illustration to make this clearer. Two people go to google.com and get google's homepage. The page should be the same for both of them. The TCP protocol might possibly break them down into different sized packets, but if we reconstruct them well, we should get the same original size for both users.

This illustration brings out an interesting point; a lot of people may go to one popular site, like google.com. To prevent this from polluting the data and clouding correlations, we removed packet sizes which were common among many users.

## Overall picture

I will now give an overview of the specific process our code goes through.

1. Read the packets from the file, splitting on direction (we didn't end up using the outbound packets).
2. Reconstruct raw packets to larger packets which are hopefully closer to their original size. We combine packets if they are within 1 second of each other and have the same animal associated with them.
3. Construct a table of animals vs packet lengths in which each cell is the number of reconstructed packets for that animal and that length.
4. From this table, construct another table of animals vs animals where each cell is the number of times a packet size was shared between the two animals.
5. Interpret the table and make predictions.

## How did we do?

We made predictions at the end of the week, and happily, we were not completely off-base. We guessed some right, and some completely off. Interestingly, the correlation we were most confident about were the phones of two of the event organizers, who were communicating a lot on google hangouts. This is important because one of the things which needs to be secured is when missionaries use low-latency communication services (similar to google hangouts).

## Nation scale

So, could our algorithms scale to the national level? Well, sort of. Our algorithm ended up being linear with the size of the packets, which is not bad. Our implementation was quite slow, but I can see improvements which could greatly optimize things and make them parallel enough to handle all of the packets created by a country. However, the algorithm was also squared time with the number of ip addresses being processed. In a whole country, there will be many millions of ip addresses, giving a trillion when squared.

## Ideas for further improvements

1. Our algorithm read the file all into memory. We could easily make it more streamable.
2. Packets could come from multiple sources, and could be reconstructed in parallel; at the national level, each district could process its own raw data, and concatenate it all in a final central table.
3. Our packet parsing algorithm was very slow, and a lot of our other code was not well optimized; it could be micro optimized a ton.
4. Don't hard code the time to split packets by (which was 1 second). Each user might have different network speeds which could affect things. Instead, analyze a sample of each users packet and fit a good time which splits packets naturally.
5. Use proven and more well-founded methods for interpreting the final table. At Code-A-Thon we did most of the interpretation by hand because we didn't find a suitable algorithm to do it (they always made one massive group with other tiny ones). Min-cuts might be something to consider.
6. Make more use of the time that people are on the internet, specifically how they relate in usage at the same time.
7. Tranform our techniques and algorithm to be more continuous; it might be insightful to be able to see correlation level in a certain window of time. Maybe we could create a graph of the correlation over time. It would be interesting to see if consistent correlation might be more telling than spikes in correlation between animals.
8. Somehow overcome the O(n^2) problem on the number of ip addresses. I feel like it should be possible to beat, maybe through some type of dynamic programming
