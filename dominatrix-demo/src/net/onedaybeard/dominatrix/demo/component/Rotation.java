/**
 * Store rotation angle in degrees.
 */
package net.onedaybeard.dominatrix.demo.component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import com.artemis.Component;

@Data @Accessors(fluent=true) @EqualsAndHashCode(callSuper=false) @AllArgsConstructor @NoArgsConstructor
public final class Rotation extends Component
{
	private float degrees;
}
