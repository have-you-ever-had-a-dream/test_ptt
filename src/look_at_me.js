import { mapValues } from "async";

// file1.txt is a file that is 1000 bytes in size
// file2.txt is a file that is 2000 bytes in size
// file3.txt is a file that is 3000 bytes in size
// file4.txt does not exist

const fileMap = {
    f1: 'file1.txt',
    f2: 'file2.txt',
    f3: 'file3.txt'
};

const withMissingFileMap = {
    f1: 'file1.txt',
    f2: 'file2.txt',
    f3: 'file4.txt'
};

// asynchronous function that returns the file size in bytes
function getFileSizeInBytes(file, key, callback) {
    fs.stat(file, function(err, stat) {
        if (err) {
            return callback(err);
        }
        callback(null, stat.size);
    });
}

// Using callbacks
async.mapValues(fileMap, getFileSizeInBytes, function(err, result) {
    if (err) {
        console.log(err);
    } else {
        console.log(result);
        // result is now a map of file size in bytes for each file, e.g.
        // {
        //     f1: 1000,
        //     f2: 2000,
        //     f3: 3000
        // }
    }
});

// Error handling
async.mapValues(withMissingFileMap, getFileSizeInBytes, function(err, result) {
    if (err) {
        console.log(err);
        // [ Error: ENOENT: no such file or directory ]
    } else {
        console.log(result);
    }
});

// Using Promises
async.mapValues(fileMap, getFileSizeInBytes)
.then( result => {
    console.log(result);
    // result is now a map of file size in bytes for each file, e.g.
    // {
    //     f1: 1000,
    //     f2: 2000,
    //     f3: 3000
    // }
}).catch (err => {
    console.log(err);
});

// Error Handling
async.mapValues(withMissingFileMap, getFileSizeInBytes)
.then( result => {
    console.log(result);
}).catch (err => {
    console.log(err);
    // [ Error: ENOENT: no such file or directory ]
});

// Using async/await
async () => {
    try {
        let result = await async.mapValues(fileMap, getFileSizeInBytes);
        console.log(result);
        // result is now a map of file size in bytes for each file, e.g.
        // {
        //     f1: 1000,
        //     f2: 2000,
        //     f3: 3000
        // }
    }
    catch (err) {
        console.log(err);
    }
}

// Error Handling
async () => {
    try {
        let result = await async.mapValues(withMissingFileMap, getFileSizeInBytes);
        console.log(result);
    }
    catch (err) {
        console.log(err);
        // [ Error: ENOENT: no such file or directory ]
    }
}

