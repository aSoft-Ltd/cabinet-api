package cabinet

import cabinet.internal.AbstractRootDir
import kollections.List
import kollections.Map
import kollections.entries
import kollections.forEach
import kollections.key
import kollections.toIMap
import kollections.toMap
import kollections.value
import koncurrent.Later
import koncurrent.PendingLater
import koncurrent.later.await
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FakeRootDir(val config: FakeRootConfig) : AbstractRootDir() {
    private val max get() = config.max
    private val scope get() = config.scope
    override fun upload(params: Array<FileUploadParam>): Map<FileUploadParam, Later<Attachment>> {
        val paramLaterMap = params.associateWith { PendingLater<Attachment>() }.toMap()
        paramLaterMap.entries.forEach { entry ->
            val param = entry.key
            val later = entry.value
            val (uploading) = later.setStages("Uploading ${config.path}/${param.path}")
            scope.launch {
                for (i in 0..max) {
                    delay(100)
                    uploading(i.toLong(), max.toLong())
                }
                val attachment = Attachment(
                    uid = "fake-attachment-${attachmentCount.incrementAndGet()}",
                    name = param.filename,
                    url = param.path,
                    sizeInBytes = param.file.readBytes(config.executor).await().size,
                )

                later.resolveWith(attachment)
            }
        }
        return paramLaterMap
    }

    override fun list(): Later<List<Attachment>> {
        TODO("Not implemented yet")
    }

    override fun delete(attachment: Attachment): Later<Attachment> {
        TODO("Not implemented yet")
    }

    companion object {
        val attachmentCount = atomic(0)
    }
}