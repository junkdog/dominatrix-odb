package net.onedaybeard.dominatrix.demo.component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.artemis.Component;

@Data @EqualsAndHashCode(callSuper=false) @AllArgsConstructor @NoArgsConstructor
public final class AssetReference extends Component
{
	private String path;
}
