import org.jenkinsci.plugins.pipeline.modeldefinition.Utils
import org.jenkinsci.plugins.pipeline.modeldefinition.when.utils.Comparator

import static ObjectModel.Step

def call(ObjectModel model, def image2Container) {

    def link = ""
    image2Container.each { image, container ->
        link += " --link ${container.id}:${image.split(":")[0]}"
    }

    docker.image(model.image).inside(link) {

        model.steps.each { step ->

            if (step.isParallelStep()) {

                def stages = [:]
                step.parallel.each { pStep ->
                    stages[pStep.name] = { buildStep(pStep) }
                }
                parallel(stages)

            } else {
                stage(step.name) {
                    buildStep(step)
                }
            }
        }
    }
}

private def buildStep(Step step) {
    if (step.except.size() > 0 && globContains(step.except, env.BRANCH_NAME)) {
        Utils.markStageSkippedForConditional(env.STAGE_NAME)
        return
    }

    if (step.only.size() > 0 && !globContains(step.only, env.BRANCH_NAME)) {
        Utils.markStageSkippedForConditional(env.STAGE_NAME)
        return
    }

    if (step.waitOnInput()) {
        input()
    }

    step.actions.each { action ->
        sh action
    }
}

private static boolean globContains(List<String> list, String e) {
    return list.find { Comparator.GLOB.compare(it, e) } != null
}
