package dev.rdh.sarcio;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Names the patch a mixin, or one of its members, belongs to, so other mods can turn it off with
 * {@code "sarcio:disable"}. A feature on a type drops the whole mixin; one on a member removes just that
 * member. Anything that must not outlive the rest of a patch has to carry the same name.
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
public @interface Feature {
	String value();
}
