package net.onedaybeard.dominatrix.demo.component;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import com.artemis.Component;

@Data @EqualsAndHashCode(callSuper=false) @Accessors(fluent=true)
public class Scale extends Component
{
	private float value = 1;
}
