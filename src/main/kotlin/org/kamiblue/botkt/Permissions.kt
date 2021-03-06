package org.kamiblue.botkt

import net.ayataka.kordis.entity.message.Message
import net.ayataka.kordis.entity.user.User
import org.kamiblue.botkt.manager.managers.ConfigManager
import org.kamiblue.botkt.utils.Colors
import org.kamiblue.botkt.utils.StringUtils.toHumanReadable

object Permissions {

    fun User?.hasPermission(permission: PermissionTypes): Boolean {
        if (this == null) return false
        return ConfigManager.readConfigSafe<PermissionConfig>(ConfigType.PERMISSION, false)?.let {
            it.councilMembers[this.id]?.contains(permission)
        } ?: false
    }

    internal suspend fun Message.missingPermissions(permission: PermissionTypes) {
        this.channel.send {
            embed {
                title = "Missing permission"
                description = "Sorry, but you're missing the '${permission.name.toHumanReadable()}' permission, which is required to run this command."
                color = Colors.ERROR.color
            }
        }
    }
}

enum class PermissionTypes {
    ARCHIVE_CHANNEL,
    COUNCIL_MEMBER,
    REBOOT_BOT,
    MANAGE_CONFIG,
    UPDATE_COUNTERS,
    SAY,
    MANAGE_CHANNELS,
    MASS_BAN,
    AUTHORIZE_CAPES,
    PURGE_PROTECTED,
    MANAGE_PLUGINS,
    VIEW_LOGS
}
