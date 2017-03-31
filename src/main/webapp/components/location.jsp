<!-- ko with: location -->
<div class="row">
	<div class="col-sm-6 form-group">
		<dfn data-bind="popover: oeca.cgp.definitions.latLong"><span class="glyphicon glyphicon-info-sign"></span></dfn>
		<label class="control-label" for="latitude">Latitude</label>
		<div class="input-group">
			<input id="latitude" class="form-control" type="text" data-bind="maskedLatLong: latitudeDisplay" />
			<div class="input-group-btn">
				<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">&deg; <span data-bind="text: latitudeDir"></span> <span class="caret"></span></button>
				<ul class="dropdown-menu" style="min-width: 50px;">
					<li><a href="JavaScript:;" data-bind="click: function(){latitudeDir('N');}">&deg; N</a></li>
					<li><a href="JavaScript:;" data-bind="click: function(){latitudeDir('S');}">&deg; S</a></li>
				</ul>
			</div>
		</div>
	</div>
	<div class="col-sm-6 form-group">
		<dfn data-bind="popover: oeca.cgp.definitions.latLong"><span class="glyphicon glyphicon-info-sign"></span></dfn>
		<label class="control-label" for="longitude">Longitude</label>
		<div class="input-group">
			<input id="longitude" class="form-control" type="text" data-bind="maskedLatLong: longitudeDisplay" />
			<div class="input-group-btn">
				<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">&deg; <span data-bind="text: longitudeDir"></span> <span class="caret"></span></button>
				<ul class="dropdown-menu" style="min-width: 50px;">
					<li><a href="JavaScript:;" data-bind="click: function(){longitudeDir('W');}">&deg; W</a></li>
					<li><a href="JavaScript:;" data-bind="click: function(){longitudeDir('E');}">&deg; E</a></li>
				</ul>
			</div>
		</div>
	</div>
</div>
<div class="row">
	<div class="col-sm-6 form-group">
		<label class="control-label">Latitude/Longitude Data Source</label>
		<div class="radio">
			<label class="radio-inline">
				<input type="radio" name="lat-long-data-source" data-bind="checkedValue: oeca.cgp.constants.latLongDataSource.Map, checked: latLongDataSource" />
				Map
			</label>
			<label class="radio-inline">
				<input type="radio" name="lat-long-data-source" data-bind="checkedValue: oeca.cgp.constants.latLongDataSource.GPS, checked: latLongDataSource" />
				GPS
			</label>
			<label class="radio-inline">
				<input type="radio" name="lat-long-data-source" data-bind="checkedValue: otherCheckedValue, checked: latLongDataSource" />
				Other
			</label>
		</div>
	</div>
	<div class="col-sm-6 form-group required" data-bind="fadeVisible: latLongDataSource() == otherCheckedValue()">
		<label class="control-label" for="other-data-source">Other Data Source</label>
		<input id="other-data-source" class="form-control" type="text" maxlength="100" data-bind="value: otherCheckedValue"/>
	</div>
</div>
<div class="row">
	<div class="col-sm-12 form-group">
		<label class="control-label">Horizontal Reference Datum
			<dfn data-bind="popover: oeca.cgp.definitions.mapWidget"><span class="glyphicon glyphicon-info-sign"></span></dfn>
		</label>
		<div class="radio">
			<label class="radio-inline">
				<input type="radio" name="ref-datum" data-bind="checkedValue: 'NAD 27', checked: horizontalReferenceDatum" />
				NAD 27
			</label>
			<label class="radio-inline">
				<input type="radio" name="ref-datum" data-bind="checkedValue: 'NAD 83', checked: horizontalReferenceDatum" />
				NAD 83
			</label>
			<label class="radio-inline">
				<input type="radio" name="ref-datum" data-bind="checkedValue: 'WGS 84', checked: horizontalReferenceDatum" />
				WGS 84
			</label>
		</div>
	</div>
</div>
<!-- /ko -->