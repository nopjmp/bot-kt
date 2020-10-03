import helpers.ShellHelper.bash
import net.ayataka.kordis.entity.message.Message
import java.io.IOException

object Send {
    fun log(message: String) = println("[bot-kt] $message")

    suspend fun Message.normal(description: String, title: String) = channel.send {
        embed {
            this.title = title
            this.description = description
            this.color = Colors.primary
        }
    }

    suspend fun Message.normal(description: String) = channel.send {
        embed {
            this.description = description
            this.color = Colors.primary
        }

    }

    suspend fun Message.success(description: String) = channel.send {
        embed {
            this.description = description
            color = Colors.success
        }
    }


    suspend fun Message.error(description: String) = channel.send {
        embed {
            this.title = "Error"
            this.description = description
            this.color = Colors.error
        }
    }


    suspend fun Message.stackTrace(e: Exception) = channel.send {
        embed {
            title = "Error"
            description = "```" + e.message + "```\n```" + e.stackTrace.joinToString("\n") + "```"
            color = Colors.error
        }
    }

    /**
     * Run a command and send the stdout / stderr to the channel
     * @return false if command errored
     */
    suspend fun String.bash(message: Message): Boolean {
        return try {
            message.normal(this.bash())
            true
        } catch (e: IOException) {
            message.stackTrace(e)
            false
        }
    }
}