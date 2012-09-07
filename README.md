Sosueme (pronounced "So, sue me!") is a collection of Clojure functions for things we like to do.

# Setup

The jar is available on Clojars at http://clojars.org/factual/sosueme .

Include it in your project.clj :dependencies:

```clojure
[factual/sosueme "0.0.14"]
```

# Contents

Sosueme provides the following functionalily, organized by namesapce:

## conf namespace

Simple convenience functions for loading in file and resource based configuration data.

### <tt>load-when</tt>

Takes a path to a file on the classpath, interprets the contents of the file as Clojure data, and
returns the result. Example:

```clojure
(println (:host (load-when "myconf.clj")))
```

### <tt>load-file!</tt>

Loads and merges configuration from a specified path into current configuration data. This
allows you to conveniently build up configuration data and refer to it from anywhere in your
application.

### <tt>get-key</tt>

Returns the value of a specific key, from current configuration data.

### <tt>all</tt>

Returns all configuration data that was loaded via <tt>load-file!</tt>.

### Example Use Case

Say you have a file called myconf.clj with this content...

```clojure
{:host "my.server.com"
 :port 8081}
```

...then your app could do this:

```clojure
(conf/load-file! "myconf.clj")
(let [host (conf/get-key :host)]
  ... )
```

## hawaii namespace

Hawaiian style functions!

### <tt>newname</tt>

Example:

```clojure
> (repeatedly 6 newname)
("kiwihei" "nehe" "naimaiku" "lahehei" "wenani" "haikimi")
```

## io namespace

I/O stuff, e.g. load content from the classpath.

### <tt>slurp-cp</tt>

Slurps content from the classpath. Example:

```clojure
(slurp-cp "somefile.txt")
```

## net namespace

Nnetworking stuff, e.g. get local hostname.

### <tt>local-hostname</tt>

Returns your local host name.

## sys namespace

System stuff, e.g. get current process id.

### <tt>pid</tt>

Returns the current process's PID, as a String.

## time namespace

Time stuff, e.g. get Unix epoch value for "25 minutes ago".

### <tt>now</tt>

Returns the Unix epoch value for the current time.

### <tt>minutes-ago</tt>

Returns the Unix epoch value for N minutes ago. Example:

```clojure
(minutes-ago 25)
```

## txt namespace

Text handling stuff.

### <tt>pretty</tt>

Uses Clojure's pprint to return a prettily formatted String representation of data, including line breaks for readability.

Example use:

```clojure
(println (pretty {:a "b" :b "this is b it is a long thing" :c {:x "X" :y '(this is a long list of things)}}))
```

# License

The use and distribution terms for this software are covered by the
Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
which can be found in the file LICENSE.html at the root of this distribution.
By using this software in any fashion, you are agreeing to be bound by
the terms of this license.
You must not remove this notice, or any other, from this software.
