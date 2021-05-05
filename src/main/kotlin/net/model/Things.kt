package net.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*


@Serializable
sealed class Post {
    /** This item's identifier, e.g. "8xwlg" */
    abstract val id: String

    /** Fullname of comment, e.g. "t1_c3v7f8u" */
    abstract val name: String
}

@Serializable
data class Comment(
    override val id: String,
    override val name: String,
    override val ups: Int,
    override val downs: Int,
    override val likes: Boolean?,
    override val created: Double,

    /** Who approved this comment. null if nobody or you are not a mod */
    @SerialName("approved_by")
    val approvedBy: String?,

    /** The account name of the poster */
    val author: String,

    /** The CSS class of the author's flair. subreddit specific */
    @SerialName("author_flair_css_class")
    val authorFlairCssClass: String?,

    /** The text of the author's flair. subreddit specific */
    @SerialName("author_flair_text")
    val authorFlairText: String?,

    /** Who removed this comment. Null if nobody or you are not a mod */
    @SerialName("banned_by")
    val bannedBy: String?,

    /**
     * The raw text.
     *
     * This is the unformatted text which includes the raw markup characters such as ** for bold.
     *
     * <, >, and & are escaped. */
    val body: String,

    /**
     * The formatted HTML text as displayed on reddit.
     *
     * For example, text that is emphasised by * will now have <em> tags wrapping it.
     * Additionally, bullets and numbered lists will now be in HTML list format.
     *
     * NOTE: The HTML string will be escaped. You must unescape to get the raw HTML.
     */
    @SerialName("body_html")
    val bodyHtml: String,

    /** The number of times this comment received reddit gold */
    @SerialName("gilded")
    val gildedCount: Int,

    /** ID of the link this comment is in */
    @SerialName("link_id")
    val linkId: String,

    /** How many times this comment has been reported, null if not a mod */
    @SerialName("num_reports")
    val reportCount: Int?,

    /** ID of the thing this comment is a reply to, either the link or a comment in it */
    @SerialName("parent_id")
    val parentId: String,

    /** True if this post is saved by the logged in user */
    val saved: Boolean,

    /** The net-score of the comment */
    val score: Int,

    /** Whether the comment's score is currently hidden */
    @SerialName("score_hidden")
    val isScoreHidden: Boolean,

    /** Subreddit of the thing excluding the /r/ prefix. "pics" */
    val subreddit: String,

    /** The ID of the subreddit in which the thing is located */
    @SerialName("subreddit_id")
    val subredditId: String,

    /**
     * To allow determining whether they have been distinguished by moderators/admins.
     *
     * null = not distinguished.
     *
     * moderator = the green [M].
     *
     * admin = the red [A].
     *
     * special = various other special distinguishes http://redd.it/19ak1b
     */
    val distinguished: String?

) : Post(), Votable, Created

@Serializable
data class Link(
    override val id: String,
    override val ups: Int,
    override val downs: Int,
    override val likes: Boolean?,
    override val created: Double,
    override val name: String,

    /** The account name of the poster. Null if this is a promotional link. */
    val author: String,

    /** The CSS class of the author's flair. Subreddit specific. */
    @SerialName("author_flair_css_class")
    val authorFlairCssClass: String?,

    /** The text of the author's flair. Subreddit specific */
    @SerialName("author_flair_text")
    val authorFlairText: String?,

    /** The domain of this link. Self posts will be `self.<subreddit>` while
     * other examples include `en.wikipedia.org` and `s3.amazon.com`.
     */
    val domain: String,

    /** True if the post is hidden by the logged in user. False if not logged in
     * or no hidden.
     */
    @SerialName("hidden")
    val isHidden: Boolean,

    @SerialName("is_self")
    val isSelfPost: Boolean,

    @SerialName("link_flair_css_class")
    val linkFlairCssClass: String?,

    @SerialName("link_flair_text")
    val linkFlairText: String?,

    @SerialName("locked")
    val isLocked: Boolean,

    /** The number of comments that belong to this link.
     * Includes removed comments.
     */
    @SerialName("num_comments")
    val commentCount: Int,

    @SerialName("over_18")
    val isNsfw: Boolean,

    /** Relative URL of the permanent link for this link. */
    val permalink: String,

    @SerialName("saved")
    val isSaved: Boolean,

    /**
     * The net-score of the link.
     *
     * A submission's score is simply the number of upvotes minus the number of downvotes.
     * If five users like the submission and three users don't it will have a score of 2.
     *
     * Please note that the vote numbers are not "real" numbers, they have been "fuzzed" to prevent spam bots etc.
     *
     * So taking the above example, if five users upvoted the submission, and three users downvote it,
     * the upvote/downvote numbers may say 23 upvotes and 21 downvotes, or 12 upvotes, and 10 downvotes.
     *
     * The points score is correct, but the vote totals are "fuzzed".
     */
    val score: Int,

    /**
     * The raw text.
     *
     * This is the unformatted text which includes the raw markup characters such
     * as ** for bold. <, >, and & are escaped.
     *
     * Empty if not present.
     */
    val selftext: String,

    /**
     * The formatted escaped HTML text.
     *
     * This is the HTML formatted version of the marked up text.
     * Items that are boldened by ** or *** will now have <em> or *** tags on them.
     *
     * Additionally, bullets and numbered lists will now be in HTML list format.
     *
     * NOTE: The HTML string will be escaped. You must unescape to get the raw HTML.
     *
     * Null if not present.
     */
    @SerialName("selftext_html")
    val selftextHtml: String?,

    /** Subreddit of the thing excluding the /r/ prefix, e.g., "pics" */
    val subreddit: String,

    /** The ID of the subreddit in which the thing is located */
    @SerialName("subreddit_id")
    val subredditId: String,

    /** The Full URL to the thumbnail for this link.
     *
     * "self" if this is a self post.
     *
     * "image" if this is a link to an image but has no thumbnail.
     *
     * "default" if a thumbnail is not available.
     */
    val thumbnail: String,


    /** The title of the link. May contain newlines for some reason. */
    val title: String,

    /** The link of this post. The Permalink if this is a self post. */
    val url: String,

    /**
     * To allow determining whether they have been distinguished by moderators/admins.
     *
     * null = not distinguished.
     *
     * moderator = the green [M].
     *
     * admin = the red [A].
     *
     * special = various other special distinguishes http://redd.it/19ak1b
     */
    val distinguished: String?,

    @SerialName("stickied")
    val isStickied: Boolean

) : Post(), Votable, Created


