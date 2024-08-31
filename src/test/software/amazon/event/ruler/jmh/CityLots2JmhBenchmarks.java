package software.amazon.event.ruler.jmh;

import static java.util.concurrent.TimeUnit.SECONDS;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.Timeout;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;


@BenchmarkMode(Mode.Throughput)
@Fork(value = 1, jvmArgsAppend = {
        "-Xmx2g", "-Xms2g", "-XX:+AlwaysPreTouch", "-XX:+UseSerialGC",
        "-XX:-BackgroundCompilation", //"-XX:TieredStopAtLevel=1",
})
@Timeout(time = 90, timeUnit = SECONDS)
@OperationsPerInvocation(213068) // Number of entries in citylots2 dataset.
public class CityLots2JmhBenchmarks {

    @Benchmark
    @Warmup(iterations = 6, batchSize = 1, time = 10, timeUnit = SECONDS)
    @Measurement(iterations = 6, batchSize = 1, time = 10, timeUnit = SECONDS)
    public void group01Simple(MachineStateSimple machineState, CityLots2State cityLots2State, Blackhole blackhole)
            throws Exception {
        run(machineState, cityLots2State, blackhole);
    }

//    @Benchmark
    @Warmup(iterations = 2, batchSize = 1, time = 45, timeUnit = SECONDS)
    @Measurement(iterations = 4, batchSize = 1, time = 45, timeUnit = SECONDS)
    public void group02Complex(MachineStateComplex machineState, CityLots2State cityLots2State, Blackhole blackhole)
            throws Exception {
        run(machineState, cityLots2State, blackhole);
    }

    private void run(MachineState machineState, CityLots2State cityLots2State, Blackhole blackhole) throws Exception {
        for (String event : cityLots2State.citylots2) {
            blackhole.consume(machineState.machine.rulesForJSONEvent(event));
        }
    }
}

