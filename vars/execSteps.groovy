import org.jenkinsci.plugins.pipeline.modeldefinition.Utils
import org.jenkinsci.plugins.pipeline.modeldefinition.when.utils.Comparator

import static ObjectModel.Step

def call(ObjectModel model, def network) {

    def args = network.getCliArgs()
    if (model.hasDockerService()) {
        args += " -v /var/run/docker.sock:/var/run/docker.sock "
    }

    docker.image(model.image).inside(args) {

        model.steps.each { step ->

            if (step.isParallelStep()) {

                def stages = [:]
                step.parallel.each { pStep ->
                    stages[pStep.name] = { buildStep(model, pStep) }
                }
                parallel(stages)

            } else {
                stage(step.name) {
                    buildStep(model, step)
                }
            }
        }
    }
}

private def buildStep(ObjectModel model, Step step) {
    if (step.except.size() > 0 && globContains(step.except, env.BRANCH_NAME)) {
        Utils.markStageSkippedForConditional(env.STAGE_NAME)
        return
    }

    if (step.only.size() > 0 && !globContains(step.only, env.BRANCH_NAME)) {
        Utils.markStageSkippedForConditional(env.STAGE_NAME)
        return
    }

    if (step.waitOnInput()) {
        try {
            timeout(time: 5, unit: 'MINUTES') {
                input()
            }
        } catch (err) {
            def user = err.getCauses()[0].getUser()
            if ('SYSTEM' == user.toString()) { // SYSTEM means timeout.
                println("build timed out")
            } else {
                userInput = false
                println("Aborted by: [${user}]")
            }
        }
    }

    step.actions.each { action ->
        model.hasDockerService() && dockerInstall()

        sh action
    }
}

private static boolean globContains(List<String> list, String e) {
    return list.find { Comparator.GLOB.compare(it, e) } != null
}
