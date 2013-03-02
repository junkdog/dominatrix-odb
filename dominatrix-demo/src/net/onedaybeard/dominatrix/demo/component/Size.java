package net.onedaybeard.dominatrix.demo.component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import com.artemis.Component;

@Data @EqualsAndHashCode(callSuper=false) @Accessors(fluent=true) @AllArgsConstructor @NoArgsConstructor
public class Size extends Component
{
	private float width;
	private float height;
}
