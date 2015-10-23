library(gplots)
data = read.csv('/Users/tianhan/Documents/ColumbiaU/Spring15/Mobile Sensing/Project/Data/new data/2015-05-02_06-48-39.csv')
data = as.matrix(data)
scaled_data = diff(data, lag = 1)
intensity = sqrt(scaled_data[,1]^2+ scaled_data[,2]^2+scaled_data[,3]^2)
intensity = c(0,intensity)
raw_data = data.frame(data,intensity)
timestamp = seq(1, length(intensity), 1)
raw_data = data.frame(raw_data, timestamp)

attach(raw_data)

k=200
datasample = rep(0,floor(length(intensity)/k))
for(i in 1:floor(length(intensity)/k))
{
	datasample[i]=max(intensity[((i-1)*k+1):((i-1)*k+k)])	
}

timestamp = seq(1, length(datasample), 1)
new_data = data.frame(datasample, timestamp)
sm = fitted(lm(datasample~poly(timestamp,25), data=new_data))

plot(timestamp,datasample,xlab="datapoint", ylab="sleep intensity",xaxt="n",type='l')


sorted = sort(sm, decreasing = TRUE)
maxsorted = max(sorted)-1/3*(max(sorted)-min(sorted))
minsorted = min(sorted)+1/3*(max(sorted)-min(sorted))
maxsortedshow = rep(maxsorted,times = length(sm))
minsortedshow = rep(minsorted,times = length(sm))
lower = min(sorted)+1/10*(max(sorted)-min(sorted))
middle = (max(sorted)+min(sorted))/2
upper = max(sorted)-1/10*(max(sorted)-min(sorted))

par(mar=c(5.1,6.1,3.1,10.1))
plot(timestamp, sm, type="l", xlab="time", ylab="",xaxt="n",yaxt="n")
lines(maxsortedshow)
lines(minsortedshow)
axis(side=2,at=c(lower,middle,upper),labels=c("Deep Sleep","Sleep","Awake"),las=2)
axis(side=4,at=c(minsorted, maxsorted),labels=c("Deep Sleep Threshold","Light Sleep Threshold"),las = 2)
axis(side=1,at=c(1, 15001, 30001, 45001, 60001, 75001, 90001),labels=c("0","50min","100min", "150min","200min","250min","300min"))

filename='non-poly diff k-200 2015-05-02_08-12-11.png'
dev.copy(device=png,file=filename,height=600,width=1200)
dev.off()

timeintv1 = 67
timeintv2 = 200
timeintv3 = 700
timeintv4 = 10

hours = length(intensity)* timeintv4 /1000/60/60
split = hours*60/50
length(sm)/split
