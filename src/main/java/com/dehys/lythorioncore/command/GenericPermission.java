package com.dehys.lythorioncore.command;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.internal.utils.Checks;
import org.bukkit.Bukkit;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;

public enum GenericPermission {

    MANAGE_CHANNEL(4, true, true, false, "Manage Channels"),
    MANAGE_SERVER(5, true, false, false, "Manage Server"),
    VIEW_AUDIT_LOGS(7, true, false, false, "View Audit Logs"),
    VIEW_CHANNEL(10, true, true, false, "View Channel(s)"),
    VIEW_GUILD_INSIGHTS(19, true, false, false, "View Server Insights"),
    MANAGE_ROLES(28, true, false, false, "Manage Roles"),
    MANAGE_PERMISSIONS(28, false, true, false, "Manage Permissions"),
    MANAGE_WEBHOOKS(29, true, true, false, "Manage Webhooks"),
    MANAGE_EMOTES_AND_STICKERS(30, true, false, false, "Manage Emojis and Stickers"),
    CREATE_INSTANT_INVITE(0, true, true, false, "Create Instant Invite"),
    KICK_MEMBERS(1, true, false, false, "Kick Members"),
    BAN_MEMBERS(2, true, false, false, "Ban Members"),
    NICKNAME_CHANGE(26, true, false, false, "Change Nickname"),
    NICKNAME_MANAGE(27, true, false, false, "Manage Nicknames"),
    MESSAGE_ADD_REACTION(6, true, true, false, "Add Reactions"),
    MESSAGE_SEND(11, true, true, false, "Send Messages"),
    MESSAGE_TTS(12, true, true, false, "Send TTS Messages"),
    MESSAGE_MANAGE(13, true, true, false, "Manage Messages"),
    MESSAGE_EMBED_LINKS(14, true, true, false, "Embed Links"),
    MESSAGE_ATTACH_FILES(15, true, true, false, "Attach Files"),
    MESSAGE_HISTORY(16, true, true, false, "Read History"),
    MESSAGE_MENTION_EVERYONE(17, true, false, true, "Mention Everyone"),
    MESSAGE_EXT_EMOJI(18, true, true, false, "Use External Emojis"),
    USE_APPLICATION_COMMANDS(31, true, true, false, "Use Application Commands"),
    MESSAGE_EXT_STICKER(37, true, true, false, "Use External Stickers"),
    MANAGE_THREADS(34, true, true, false, "Manage Threads"),
    CREATE_PUBLIC_THREADS(35, true, true, false, "Create Public Threads"),
    CREATE_PRIVATE_THREADS(36, true, true, false, "Create Private Threads"),
    MESSAGE_SEND_IN_THREADS(38, true, true, false, "Send Messages in Threads"),
    PRIORITY_SPEAKER(8, true, true, false, "Priority Speaker"),
    VOICE_STREAM(9, true, true, false, "Video"),
    VOICE_CONNECT(20, true, true, false, "Connect"),
    VOICE_SPEAK(21, true, true, false, "Speak"),
    VOICE_MUTE_OTHERS(22, true, true, false, "Mute Members"),
    VOICE_DEAF_OTHERS(23, true, true, false, "Deafen Members"),
    VOICE_MOVE_OTHERS(24, true, true, false, "Move Members"),
    VOICE_USE_VAD(25, true, true, false, "Use Voice Activity"),
    VOICE_START_ACTIVITIES(39, true, true, false, "Launch Activities in Voice Channels"),
    REQUEST_TO_SPEAK(32, true, true, false, "Request to Speak"),
    ADMINISTRATOR(3, true, false, false, "Administrator"),
    UNKNOWN(-1, false, false, false, "Unknown"),
    USE_RELOAD_COMMAND(501, false, false, true, "lythorion.reload"),
    USE_RESTART_COMMAND(502, false, false, true, "lythorion.restart"),
    USE_STAFFCHAT_COMMAND(503, false, false, true, "lythorion.staffchat"),
    USE_CLAIM_COMMAND(504, false, false, true, "lythorion.claim"),
    USE_NICK_COMMAND(505, false, false, true, "lythorion.nick"),
    USE_PLAYERS_COMMAND(506, false, false, true, "lythorion.players"),
    USE_SHOWITEM_COMMAND(507, false, false, true, "lythorion.showitem");

    public static final GenericPermission[] EMPTY_PERMISSIONS = new GenericPermission[0];
    public static final Collection<GenericPermission> ALL_PERMISSIONS = Arrays.stream(values()).toList();
    public static final Collection<GenericPermission> ALL_DISCORD_PERMISSIONS = Arrays.stream(values()).filter(p -> p.isGuild && p.isChannel && p.isText()).toList();
    public static final Collection<GenericPermission> ALL_CHANNEL_PERMISSIONS = Arrays.stream(values()).filter(p -> p.isChannel).toList();
    public static final Collection<GenericPermission> ALL_GUILD_PERMISSIONS = Arrays.stream(values()).filter(p -> p.isGuild).toList();
    public static final Collection<GenericPermission> ALL_BUKKIT_PERMISSIONS = Arrays.stream(values()).filter(p -> p.isBukkit).toList();
    public static final Collection<GenericPermission> ALL_TEXT_PERMISSIONS = Arrays.asList(MESSAGE_ADD_REACTION, MESSAGE_SEND, MESSAGE_TTS, MESSAGE_MANAGE, MESSAGE_EMBED_LINKS, MESSAGE_ATTACH_FILES, MESSAGE_EXT_EMOJI, MESSAGE_EXT_STICKER, MESSAGE_HISTORY, MESSAGE_MENTION_EVERYONE, USE_APPLICATION_COMMANDS, MANAGE_THREADS, CREATE_PUBLIC_THREADS, CREATE_PRIVATE_THREADS, MESSAGE_SEND_IN_THREADS);
    public static final Collection<GenericPermission> ALL_VOICE_PERMISSIONS = Arrays.asList(VOICE_STREAM, VOICE_CONNECT, VOICE_SPEAK, VOICE_MUTE_OTHERS, VOICE_DEAF_OTHERS, VOICE_MOVE_OTHERS, VOICE_USE_VAD, PRIORITY_SPEAKER, REQUEST_TO_SPEAK, VOICE_START_ACTIVITIES);

    private final int offset;
    private final long raw;
    private final boolean isGuild;
    private final boolean isChannel;
    private final boolean isBukkit;
    private final String name;

    GenericPermission(int offset, boolean isGuild, boolean isChannel, boolean isBukkit, @Nonnull String name) {
        this.offset = offset;
        this.raw = 1L << offset;
        this.isGuild = isGuild;
        this.isChannel = isChannel;
        this.isBukkit = isBukkit;
        this.name = name;
    }

    @Nonnull
    public String getName() {
        return this.name;
    }

    public int getOffset() {
        return this.offset;
    }

    public long getRawValue() {
        return this.raw;
    }

    public boolean isGuild() {
        return this.isGuild;
    }

    public boolean isChannel() {
        return this.isChannel;
    }

    public boolean isBukkit() {
        return this.isBukkit;
    }

    public boolean isText() {
        return ALL_TEXT_PERMISSIONS.contains(this);
    }

    public boolean isVoice() {
        return ALL_VOICE_PERMISSIONS.contains(this);
    }

    @Nonnull
    public static Permission getFromOffset(int offset) {
        GenericPermission[] var1 = values();

        for (GenericPermission genericPermission : var1) {
            Permission perm = Permission.getFromOffset(genericPermission.offset);
            if (perm.getOffset() == offset) {
                return perm;
            }
        }

        return Permission.UNKNOWN;
    }

    public static Permission getAsDiscordPermission(GenericPermission permission) {
        return Permission.getFromOffset(permission.offset);
    }

    public static org.bukkit.permissions.Permission getAsBukkitPermission(GenericPermission permission) {
        return Bukkit.getServer().getPluginManager().getPermission(permission.name);
    }

    public static GenericPermission getFromName(String name) {
        for (GenericPermission perm : values()) {
            if (perm.getName().equalsIgnoreCase(name)) {
                return perm;
            }
        }
        return UNKNOWN;
    }

    @Nonnull
    public static EnumSet<GenericPermission> getPermissions(long permissions) {
        if (permissions == 0L) {
            return EnumSet.noneOf(GenericPermission.class);
        } else {
            EnumSet<GenericPermission> perms = EnumSet.noneOf(GenericPermission.class);
            GenericPermission[] var3 = values();

            for (GenericPermission perm : var3) {
                if (perm != UNKNOWN && (permissions & perm.raw) == perm.raw) {
                    perms.add(perm);
                }
            }

            return perms;
        }
    }

    public static long getRaw(GenericPermission[] excludedPermissions, @Nonnull GenericPermission... permissions) {
        long raw = 0L;
        if (excludedPermissions == null) excludedPermissions = new GenericPermission[0];

        for (GenericPermission perm : permissions) {
            if (perm != null && perm != UNKNOWN && Arrays.stream(excludedPermissions).noneMatch(p -> p.equals(perm))) {
                raw |= perm.raw;
            }
        }

        return raw;
    }

    public static long getRaw(@Nonnull Collection<GenericPermission> permissions) {
        Checks.notNull(permissions, "Permission Collection");
        return getRaw(permissions.toArray(EMPTY_PERMISSIONS));
    }

}
