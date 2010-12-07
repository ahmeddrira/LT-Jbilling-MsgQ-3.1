<div id="plugin-parameters" class="box-card-hold">
    <div class="form-columns">
		<div class="column">
			<g:each in="${parametersDesc}">
			    <div class="row">
				<label>
					${it.name}
				</label>
				<div class="inp-bg inp4">
				  <g:textField class="field" name="plg-parm-${it.name}" id="plg-parm-${it.name}" />
				</div>
                </div>
            </g:each>
        </div>
    </div>
</div>