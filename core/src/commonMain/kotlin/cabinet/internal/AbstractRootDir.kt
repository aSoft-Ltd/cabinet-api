package cabinet.internal

import cabinet.Attachment
import cabinet.FileUploadParam
import cabinet.RootDir
import kollections.first
import kollections.listOf
import kollections.toList
import kollections.values
import koncurrent.Later

abstract class AbstractRootDir : RootDir {
    override fun upload(param: FileUploadParam): Later<Attachment> = upload(listOf(param)).values.toList().first()
}