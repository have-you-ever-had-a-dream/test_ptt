const async = require('async');
const async = require('async');

function processValues(values, callback) {
    async.mapValues(values, (value, key, next) => {
        // Perform some asynchronous operation on each value
        // ...

        // Call the callback with the processed value
        next(null, processedValue);
    }, (err, processedValues) => {
        if (err) {
            // Handle error
            callback(err);
        } else {
            // Processed values are available here
            callback(null, processedValues);
        }
    });
}

// Example usage
const values = {
    key1: 'value1',
    key2: 'value2',
    key3: 'value3'
};

processValues(values, (err, processedValues) => {
    if (err) {
        console.error('Error:', err);
    } else {
        console.log('Processed values:', processedValues);
    }
});