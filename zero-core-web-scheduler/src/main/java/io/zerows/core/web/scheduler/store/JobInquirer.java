package io.zerows.core.web.scheduler.store;

import io.vertx.up.annotations.Job;
import io.vertx.up.util.Ut;
import io.zerows.core.metadata.zdk.uca.Inquirer;
import io.zerows.core.web.model.uca.extract.Extractor;
import io.zerows.core.web.scheduler.atom.Mission;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class JobInquirer implements Inquirer<Set<Mission>> {

    private final transient Extractor<Mission> extractor = Ut.singleton(JobExtractor.class);

    @Override
    public Set<Mission> scan(final Set<Class<?>> clazzes) {
        final Set<Class<?>> jobs = clazzes.stream()
            .filter(item -> item.isAnnotationPresent(Job.class))
            .collect(Collectors.toSet());
        /* All classes of jobs here */
        this.logger().info(INFO.JOB, jobs.size());
        return jobs.stream().map(this.extractor::extract)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
    }
}
