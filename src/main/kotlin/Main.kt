import java.util.*
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sqrt

fun generateGaussian(loops: Int, aMean: Double, aVariance: Double): MutableList<Double> {
    val r = Random()
    val mutableList = mutableListOf<Double>()
    for (i in 1..loops){
        mutableList.add(aMean + r.nextGaussian() * aVariance)
    }
    return mutableList
}

fun generateWeights(number_distrobutions: Int): MutableList<Double> {
    val weights = mutableListOf<Double>()
    for (i in 1..number_distrobutions){
        weights.add(1/number_distrobutions.toDouble())
    }
    return weights
}

fun getstdev(data: MutableList<Double>): Double {
    val average = data.average()
    var sum = 0.0
    val out = data.forEach { sum = sum +(it -average).pow(2) }
    sum = sqrt((sum / data.size))
    return sum
}

fun pdf(data: MutableList<Double>, mean: Double, variance: Double): MutableList<Double> {
    val s1 = 1/(sqrt(6.283 * variance))
    val temp = mutableListOf<Double>()
    for (I in data) {
        temp.add((exp(-(I - mean).pow(2) / 2 * variance)) * s1)
    }
        return temp
    }

fun returnliklyhood(likelihood: MutableList<MutableList<Double>>, number_distrobutions: Int, loop_number: Int, samples: MutableList<Double>,
                    means: MutableList<Double>, variances: MutableList<Double>): MutableList<MutableList<Double>> {
    /**
     * This function takes a list of list where the depth of the list is the number of distributions and the
     * width of the lists s the number of samples.
     * The function accepts the list of list for the likleyhood, the samples, the means and the variances
     */
    val i = loop_number
        val temp = pdf(samples, means[i],variances[i])
        likelihood[i] = temp
return likelihood

}

fun generateliklyhood(number_distrobutions: Int, numberPoints: Int): MutableList<MutableList<Double>> {
    /** this needs to return a 2D list of lists with the length being the number of points and the height
     * being the number of distributions.
     */
    val liklyhood = MutableList(numberPoints) { mutableListOf<Double>(0.0) }

    for (i in 0 until number_distrobutions){
        liklyhood.add(MutableList(numberPoints * number_distrobutions){0.0})
    }
    for ( i in 0 until numberPoints) {
        liklyhood.removeAt(0)
    }
    return liklyhood
}

fun calculateB(number_distrobutions: Int, numberPoints: Int, loop_number: Int, weights: MutableList<Double>,
               likelihood: MutableList<MutableList<Double>>, b: MutableList<MutableList<Double>>): MutableList<MutableList<Double>> {
    var denominator = 0.0
    for (current_distibution in 0 until number_distrobutions){
        denominator += (likelihood[current_distibution].sum() * weights[current_distibution])
    }

    for (current_item in 0 until numberPoints * number_distrobutions){
        b[loop_number][current_item] = likelihood[loop_number][current_item] * (weights[loop_number])/denominator
    }

//    }
    return b
}


fun main(args: Array<String>) {
    // Inital parameters
    val numberPoints = 3
    val SampleMeans = mutableListOf<Double>(5.0, 10.0, 15.0)
    val SampleVariances = mutableListOf<Double>(1.0, 1.0, 1.0)
    val number_distrobutions = 3


    // Generate test data
    var samples = mutableListOf<Double>()
    for (i in 0 until number_distrobutions) {
        samples =
            (samples + (generateGaussian(numberPoints, SampleMeans[i], SampleVariances[i]))) as MutableList<Double>
    }

    val weights = generateWeights(number_distrobutions)
    val means = MutableList<Double>(number_distrobutions) { 0.0 }
    for (item in 0 until means.size) {
        means[item] = samples.random()
    }
    val variances = MutableList(number_distrobutions) { kotlin.random.Random.nextDouble(0.0, 1.0) }


    // Inital set up of likely hoods
    var likelyhood = generateliklyhood(number_distrobutions, numberPoints)
    var b = MutableList(0) { mutableListOf<Double>() }
    b.add(MutableList(numberPoints * number_distrobutions) { 0.0 })
    b.add(MutableList(numberPoints * number_distrobutions) { 0.0 })
    b.add(MutableList(numberPoints * number_distrobutions) { 0.0 })
//    println(b)

for (i in 0 until 100) {
    for (current_loop in 0 until 3) {
        //   val current_loop = 0

        // calculate the likely hoods
        likelyhood = returnliklyhood(likelyhood, number_distrobutions, current_loop, samples, means, variances)

        // calculate B
        val b = calculateB(number_distrobutions, numberPoints, current_loop, weights, likelyhood, b)

        // Calculate means
        val holdall = MutableList<Double>(numberPoints * number_distrobutions) { 0.0 }
        for (i in 0 until b[current_loop].size) {
            holdall[i] = b[current_loop][i] * samples[i]
        }
        means[current_loop] = holdall.sum() / b[current_loop].sum()


        val hold = MutableList<Double>(numberPoints * number_distrobutions) { 0.0 }
        for (i in 0 until b[current_loop].size) {
            hold[i] = b[current_loop][i] * ((samples[i] - means[current_loop]).pow(2))
        }
        variances[current_loop] = hold.sum() / b[current_loop].sum()

// update weight
        weights[current_loop] = b[current_loop].average()


    }
}
    println(b)
    println(variances)
    println(means)
}
//
////    }
////}