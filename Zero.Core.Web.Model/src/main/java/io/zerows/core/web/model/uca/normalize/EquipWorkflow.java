package io.zerows.core.web.model.uca.normalize;

import io.horizon.eon.VPath;
import io.vertx.up.util.Ut;
import io.zerows.core.metadata.atom.configuration.MDConfiguration;
import io.zerows.core.metadata.atom.configuration.MDId;
import io.zerows.core.metadata.atom.configuration.children.MDWorkflow;
import org.osgi.framework.Bundle;

import java.util.List;
import java.util.Objects;

/**
 * @author lang : 2024-06-17
 */
class EquipWorkflow implements EquipAt {
    @Override
    public void initialize(final MDConfiguration configuration) {

        // workflow/RUNNING/
        final MDId id = configuration.id();
        final Bundle owner = id.owner();

        final String workflowDir = id.path() + "/workflow/RUNNING";
        final List<String> workflowList = this.scanDir(workflowDir, owner);
        workflowList.stream().map(workflowEach -> {
                final String workflowPath;
                if (workflowEach.startsWith(workflowDir)) {
                    workflowPath = workflowEach;
                } else {
                    workflowPath = Ut.ioPath(workflowDir, workflowEach);
                }
                return this.buildWorkflow(workflowPath, id);
            })
            .forEach(configuration::addWorkflow);
    }

    private MDWorkflow buildWorkflow(final String workflowDir, final MDId id) {
        final MDWorkflow workflow = new MDWorkflow(id);
        final Bundle owner = id.owner();
        workflow.configure(workflowDir.trim());
        // *.form
        final List<String> formFiles = this.scanForm(workflowDir, owner, VPath.SUFFIX.BPMN_FORM);
        // *.json
        final List<String> formData = this.scanForm(workflowDir, owner, VPath.SUFFIX.JSON);
        return workflow.configure(formFiles, formData);
    }

    private List<String> scanForm(final String workflowDir, final Bundle owner,
                                  final String extension) {
        if (Objects.isNull(owner)) {
            return Ut.ioFiles(workflowDir, extension);
        } else {
            return Ut.Bnd.ioFile(workflowDir, owner, extension);
        }
    }

    private List<String> scanDir(final String workflowDir, final Bundle owner) {
        if (Objects.isNull(owner)) {
            return Ut.ioDirectories(workflowDir);
        } else {
            return Ut.Bnd.ioDirectory(workflowDir, owner);
        }
    }
}
