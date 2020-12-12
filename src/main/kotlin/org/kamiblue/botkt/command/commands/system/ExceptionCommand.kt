package org.kamiblue.botkt.command.commands.system

import org.kamiblue.botkt.PermissionTypes.COUNCIL_MEMBER
import org.kamiblue.botkt.command.BotCommand
import org.kamiblue.botkt.command.Category
import org.kamiblue.botkt.utils.MessageSendUtils.error
import org.kamiblue.botkt.utils.MessageSendUtils.normal
import org.kamiblue.botkt.utils.MessageSendUtils.success
import org.kamiblue.botkt.utils.StringUtils.flat

object ExceptionCommand : BotCommand(
    name = "exception",
    alias = arrayOf("exc"),
    category = Category.SYSTEM,
    description = "View recent exceptions caused in commands"
) {
    private val exceptions = ArrayDeque<Exception>(16)

    init {
        literal("list") {
            executeIfHas(COUNCIL_MEMBER, "List saved exceptions") {
                if (exceptions.isEmpty()) {
                    message.success("No exceptions caught recently!")
                } else {
                    message.normal(
                        exceptions.withIndex().joinToString(separator = "\n") { "`${it.index}`: `${it.value.message}`" }
                    )
                }
            }
        }

        literal("view") {
            int("index") { indexArg ->
                executeIfHas(COUNCIL_MEMBER, "Print a saved exception") {
                    if (exceptions.isEmpty()) {
                        message.success("No exceptions caught recently!")
                    } else {
                        exceptions.getOrNull(indexArg.value)?.let {
                            message.channel.send("```\n" + it.stackTraceToString().flat(1992) + "\n```")
                        } ?: run {
                            message.error("Exception with index `${indexArg.value}` is not stored!")
                        }
                    }
                }
            }
        }
    }

    fun addException(e: Exception) {
        while (exceptions.size >= 10) {
            exceptions.removeFirstOrNull()
        }
        exceptions.add(e)
    }
}