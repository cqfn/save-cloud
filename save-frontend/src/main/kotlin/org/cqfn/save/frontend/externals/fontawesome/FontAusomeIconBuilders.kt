/**
 * kotlin-react builders for FontAwesomeIcon components
 */

package org.cqfn.save.frontend.externals.fontawesome

import react.RBuilder
import react.RHandler

/**
 * @param icon icon. Can be object, string or array.
 * @param classes element's classes
 * @return ReactElement
 */
fun RBuilder.fontAwesomeIcon(
    icon: dynamic,
    classes: String = ""
) = child(FontAwesomeIcon::class) {
    attrs.icon = icon
    attrs.className = classes
}

/**
 * @param handler handler to set up a component
 * @return ReactElement
 */
fun RBuilder.fontAwesomeIcon(
    handler: RHandler<FontAwesomeIconProps>
) = child(FontAwesomeIcon::class) {
    handler.invoke(this)
}
