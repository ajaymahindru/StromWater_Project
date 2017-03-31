<!-- ko with: location -->
<div class="row">
	<div class="col-sm-12">
		<div class="form-group">
			<label class="control-label" for="latitude-longitude">Latitude/Longitude:</label>
			<span class="form-group-static" id="latitude-longitude" data-bind="html: display"></span>
		</div>
	</div>
</div>
<div class="row">
    <div class="col-sm-6">
        <div class="form-group">
            <label class="control-label" for="data-source">Latitude/Longitude Data Source:</label>
            <span class="form-group-static" id="data-source" data-bind="text: latLongDataSource" style="word-wrap: break-word"></span>
        </div>
    </div>
    <div class="col-sm-6">
        <div class="form-group">
            <label class="control-label" for="horizontal-datum">Horizontal Reference Datum:</label>
            <span class="form-group-static" id="horizontal-datum" data-bind="text: horizontalReferenceDatum"></span>
        </div>
    </div>
</div>
<!-- /ko -->